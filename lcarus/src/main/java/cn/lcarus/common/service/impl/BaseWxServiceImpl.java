package cn.lcarus.common.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.chinotan.zeus.config.constant.CommonConstant;
import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.exception.BusinessException;
import cn.chinotan.zeus.framework.util.IpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.lcarus.common.entity.LotteryActivity;
import cn.lcarus.common.entity.UserAccountRecord;
import cn.lcarus.common.entity.UserThirdInfo;
import cn.lcarus.common.entity.UserThirdNotice;
import cn.lcarus.common.enums.UserAccountRecordPayChannelEnum;
import cn.lcarus.common.enums.UserAccountRecordStateEnum;
import cn.lcarus.common.service.BaseUserAccountRecordService;
import cn.lcarus.common.service.BaseUserThirdInfoService;
import cn.lcarus.common.service.BaseUserThirdNoticeService;
import cn.lcarus.common.service.BaseWxService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.request.WxPaySendRedpackRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRedpackQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPaySendRedpackResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.RedpackService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @program: guess
 * @description: 微信接口实现
 * @author: xingcheng
 * @create: 2019-09-01 20:14
 **/
@Slf4j
@Service
public class BaseWxServiceImpl implements BaseWxService {

    @Autowired
    private WxMaMsgService wxMaMsgService;

    @Autowired
    private RedpackService redpackService;

    @Autowired
    @Lazy
    private BaseUserThirdInfoService baseUserThirdInfoService;

    @Autowired
    private BaseUserThirdNoticeService baseUserThirdNoticeService;

    @Autowired
    private BaseUserAccountRecordService baseUserAccountRecordService;

    @Autowired
    @Lazy
    private BaseWxService baseWxService;

    @Value("${wx.mp.appId}")
    private String mpAppId;

    @Value("${wx.miniapp.appid}")
    private String miniAppAppId;

    @Value("${lcarus.wx.miniapp.index-page}")
    private String indexPage;

    @Value("${lcarus.wx.miniapp.notice.mini-program-state}")
    private String miniProgramNoticeState;

    @Value("${lcarus.wx.miniapp.subscribe-message-page}")
    private String subscribeMessagePage;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> sendRealAmount(Long userId, BigDecimal amount, String appId, Long userAccountRecordId) {
        Assert.notNull(userId, "userId");
        Assert.notNull(amount, "amount");
        Assert.notNull(appId, "appId");
        Assert.notNull(userAccountRecordId, "userAccountRecordId");
        // 校验appId
        Assert.isTrue(Objects.equals(appId, mpAppId), ApiCode.ERROR_6408.getMsg());

        // 查询3方用户信息
        UserThirdInfo userThirdInfo = baseUserThirdInfoService.getUserThirdInfo(userId, appId);
        String openId = userThirdInfo.getOpenId();
        Assert.isTrue(Objects.nonNull(openId), ApiCode.ERROR_6403.getMsg());

        DateTime now = DateUtil.date();
        WxPaySendRedpackRequest wxPaySendRedpackRequest = WxPaySendRedpackRequest
                .newBuilder()
                .totalAmount(amount.multiply(BigDecimal.valueOf(CommonConstant.DEFAULT_100)).intValue())
                .totalNum(CommonConstant.DEFAULT_1)
                .reOpenid(openId)
                .mchBillNo(userAccountRecordId.toString())
                .sendName("小勾搭plus")
                .wxAppid(appId)
                .sceneId("PRODUCT_2")
                .wishing("提现红包已到账，请立即查看！")
                .clientIp(IpUtil.getLocalhostIp())
                .actName("天天领奖品")
                .build();
        WxPaySendRedpackResult wxPaySendRedpackResult = null;
        try {
            wxPaySendRedpackResult = redpackService.sendRedpack(wxPaySendRedpackRequest);
        } catch (WxPayException e) {
            log.error("sendRealAmount send WxPayException, userId {}, userAccountRecordId {}", userId, userAccountRecordId, e);
            String returnCode = e.getReturnCode();
            String resultCode = e.getResultCode();
            String errCode = e.getErrCode();
            // 校验returnCode 此字段是通信标识，非红包发放结果标识，红包发放是否成功需要查看result_code来判断
            if (Objects.equals(WxPayConstants.ResultCode.FAIL, StringUtils.trimToEmpty(returnCode).toUpperCase())) {
                log.error("sendRealAmount send returnCode error, returnCode {}, return_msg {}, WxPayException {}, userId {}, userAccountRecordId {}", returnCode, e.getReturnMsg(), JSON.toJSONString(e), userId, userAccountRecordId);
                throw new BusinessException(ApiCode.ERROR_6305);
            }
            // 校验resultCode 当状态为FAIL时，存在业务结果未明确的情况。所以如果状态是FAIL请务必再请求一次查询接口[请务必关注错误代码]
            if (Objects.equals(WxPayConstants.ResultCode.FAIL, StringUtils.trimToEmpty(resultCode).toUpperCase())) {
                log.error("sendRealAmount send resultCode error, errCode {}, return_msg {}, WxPayException {}, userId {}, userAccountRecordId {}", errCode, e.getErrCodeDes(), JSON.toJSONString(e), userId, userAccountRecordId);
                switch (errCode) {
                    case CA_ERROR:
                        baseUserAccountRecordService.updateUserAccountRecordState(userAccountRecordId, UserAccountRecordStateEnum.ING_FAIL.getCode(), UserAccountRecordPayChannelEnum.WX_MP_RED.getCode(), e.getErrCodeDes());
                        return ApiResult.fail(ApiCode.ERROR_6307);
                    case SYSTEM_ERROR:
                    case PROCESSING:
                        baseUserAccountRecordService.updateUserAccountRecordState(userAccountRecordId, UserAccountRecordStateEnum.ING.getCode(), UserAccountRecordPayChannelEnum.WX_MP_RED.getCode(), e.getErrCodeDes());
                        return ApiResult.fail(ApiCode.ERROR_6308);
                    case SEND_FAILED:
                        baseUserAccountRecordService.updateUserAccountRecordFinalFail(userAccountRecordId, UserAccountRecordPayChannelEnum.WX_MP_RED.getCode(), e.getErrCodeDes());
                        return ApiResult.fail(ApiCode.ERROR_6309);
                    default:
                        log.error("sendRealAmount send errCode is real error and not to deal and need rollback try, errCode {}, WxPayException {}, userId {}, userAccountRecordId {}",
                                errCode, JSON.toJSONString(e), userId, userAccountRecordId);
                        throw new BusinessException(ApiCode.ERROR_6305);
                }
            }
            throw new BusinessException(ApiCode.ERROR_6305);
        } catch (Exception e) {
            log.error("sendRealAmount send Exception, userId {}, userAccountRecordId {}", userId, userAccountRecordId, e);
            throw new BusinessException(ApiCode.ERROR_6305);
        }
        if (Objects.isNull(wxPaySendRedpackResult)) {
            log.error("sendRealAmount send wxPaySendRedpackResult is null, userId {}, userAccountRecordId {}", userId, userAccountRecordId);
            throw new BusinessException(ApiCode.ERROR_6305);
        }
        String sendListId = wxPaySendRedpackResult.getSendListid();
        // 微信支付成功
        baseUserAccountRecordService.updateUserAccountRecordStateSuccess(userAccountRecordId, UserAccountRecordPayChannelEnum.WX_MP_RED.getCode(), sendListId);
        return ApiResult.ok();
    }

    private boolean sendSubscribeMessage(WxMaSubscribeMessage wxMaSubscribeMessage, String openId) {
        if (StringUtils.isBlank(openId)) {
            log.error("sendWATemplate openId is blank {}", openId);
            return false;
        }
        wxMaSubscribeMessage.setToUser(openId);
        try {
            if (StrUtil.isNotBlank(miniProgramNoticeState)) {
                if (StrUtil.equals(miniProgramNoticeState, WxMaConstants.MiniprogramState.FORMAL) ||
                        StrUtil.equals(miniProgramNoticeState, WxMaConstants.MiniprogramState.TRIAL) ||
                        StrUtil.equals(miniProgramNoticeState, WxMaConstants.MiniprogramState.DEVELOPER)) {
                    wxMaSubscribeMessage.setMiniprogramState(miniProgramNoticeState);
                }
            }
            wxMaMsgService.sendSubscribeMsg(wxMaSubscribeMessage);
            return true;
        } catch (WxErrorException e) {
            log.error("sendWATemplate send WxErrorException {}", e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("sendWATemplate send Exception", e);
        }
        return false;
    }

    @Override
    public void sendAllUserThirdNoticeWATemplate(WxMaSubscribeMessage wxMaSubscribeMessage) {
        if (Objects.isNull(wxMaSubscribeMessage)) {
            log.error("sendWATemplate wxMaSubscribeMessage is null");
            return;
        }

        int appCount = baseUserThirdNoticeService
                .lambdaQuery()
                .eq(UserThirdNotice::getTemplateId, wxMaSubscribeMessage.getTemplateId())
                .eq(UserThirdNotice::getAppId, miniAppAppId)
                .gt(UserThirdNotice::getEnableSendNum, CommonConstant.DEFAULT_LONG_ZERO)
                .count();
        int size = (appCount / DEFAULT_SIZE) + 1;
        for (int i = 1; i <= size; i++) {
            IPage<UserThirdNotice> page = baseUserThirdNoticeService
                    .lambdaQuery()
                    .eq(UserThirdNotice::getTemplateId, wxMaSubscribeMessage.getTemplateId())
                    .eq(UserThirdNotice::getAppId, miniAppAppId)
                    .gt(UserThirdNotice::getEnableSendNum, CommonConstant.DEFAULT_LONG_ZERO)
                    .select(UserThirdNotice::getOpenId, UserThirdNotice::getUserId)
                    .page(new Page<>(i, DEFAULT_SIZE));
            List<UserThirdNotice> records = page.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                records.parallelStream()
                        .forEach(userThirdNotice -> {
                            String openId = userThirdNotice.getOpenId();
                            sendSubscribeMessage(wxMaSubscribeMessage, openId);
                            baseUserThirdNoticeService.updateUserThirdNoticeEnableSendNum(wxMaSubscribeMessage.getTemplateId(), miniAppAppId, userThirdNotice.getUserId(), CommonConstant.DEFAULT_LONG_NEGATIVE_1);
                        });
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reSendRealAmount(UserAccountRecord userAccountRecord) throws WxPayException {
        Assert.notNull(userAccountRecord, "userAccountRecord");
        Long userId = userAccountRecord.getUserId();
        Long id = userAccountRecord.getId();
        BigDecimal amount = userAccountRecord.getAmount();
        // 查询红包发放状态
        WxPayRedpackQueryResult wxPayRedpackQueryResult = redpackService.queryRedpack(String.valueOf(id));
        String status = wxPayRedpackQueryResult.getStatus();
        switch (status) {
            case CommonConstant.FAILED:
                /// 重新发起提现
                log.info("UserAccountRecordWithDrawIngFailTask-reSendRealAmount userAccountRecord {}", JSON.toJSONString(userAccountRecord));
                ApiResult<?> sendResult = baseWxService.sendRealAmount(userId, amount, mpAppId, id);
                if (sendResult.isSuccess()) {
                    baseWxService.sendWithDrawSuccessNotice(userId, amount);
                } else {
                    if (Objects.equals(sendResult.getCode(), ApiCode.CODE_100304.getCode())) {
                        // 如果提现已经失败，则恢复提现账户，可以重新发起提现
                        // TODO: 2020/3/21 暂时不恢复额度 只打印日志 线下处理 
                        // userAccountService.changeUserAccountAmount(userId, amount);
                        log.error("UserAccountRecordWithDrawIngFailTask-reSendRealAmount final fail userAccountRecord {}", JSON.toJSONString(userAccountRecord));
                    }
                }
                break;
            default:
                log.warn("UserAccountRecordWithDrawIngFailTask-not to deal status {}, userId {} userAccountRecord {}, wxPayRedpackQueryResult {}",
                        status, userAccountRecord.getUserId(), JSON.toJSONString(userAccountRecord), JSON.toJSONString(wxPayRedpackQueryResult));
                break;
        }
    }

    @Override
    public void sendWithDrawSuccessNotice(Long userId, BigDecimal amount) {
        if (Objects.isNull(userId) || Objects.isNull(amount)) {
            log.error("BaseWxServiceImpl-sendWithDrawSuccessNotice userId or amount is null, userId {}, amount {}", userId, amount);
            return;
        }
        try {
            List<WxMaSubscribeMessage.Data> dataList = Lists.newArrayList();
            WxMaSubscribeMessage.Data data = new WxMaSubscribeMessage.Data();
            // 提现状态
            data.setName("thing1");
            data.setValue("提现成功");
            dataList.add(data);
            // 提现金额
            data = new WxMaSubscribeMessage.Data();
            data.setName("amount2");
            data.setValue(amount.toString() + "元");
            dataList.add(data);
            // 时间
            data = new WxMaSubscribeMessage.Data();
            data.setName("time3");
            data.setValue(DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_PATTERN));
            dataList.add(data);
            // 备注
            data = new WxMaSubscribeMessage.Data();
            data.setName("thing4");
            data.setValue("小勾搭plus现金奖励红包");
            dataList.add(data);
            WxMaSubscribeMessage wxMaSubscribeMessage = WxMaSubscribeMessage
                    .builder()
                    .page(indexPage)
                    .data(dataList)
                    .templateId("RJOt70YNR7wvHSlW4D3c7rKuIrgrFelGmoEGO4VogEQ")
                    .build();
            baseWxService.sendWATemplate(userId, wxMaSubscribeMessage);
        } catch (Exception e) {
            log.error("BaseWxServiceImpl-sendWithDrawSuccessNotice Exception userId {}, amount {}", userId, amount, e);
        }
    }

    @Override
    @Async
    public void sendOpenLotteryNotice(LotteryActivity lotteryActivity, DateTime now, Set<Long> waitSendNoticeUserIdSet) {
        if (CollUtil.isNotEmpty(waitSendNoticeUserIdSet)) {
            try {
                String templateId = "SVMifRxwdZyFlfmeW7ytZ1_h1wH7IqjflN32FHJyDpc";
                List<UserThirdNotice> enableUserThirdNoticeList = baseUserThirdNoticeService.getEnableUserThirdNoticeList(waitSendNoticeUserIdSet, templateId, miniAppAppId);
                if (CollUtil.isEmpty(enableUserThirdNoticeList)) {
                    log.warn("sendWATemplate enableUserThirdNoticeList is null");
                    return;
                }
                List<WxMaSubscribeMessage.Data> dataList = Lists.newArrayList();
                // 活动商品
                WxMaSubscribeMessage.Data data = new WxMaSubscribeMessage.Data();
                data.setName("thing5");
                data.setValue(lotteryActivity.getName());
                dataList.add(data);
                // 注意事项
                data = new WxMaSubscribeMessage.Data();
                data.setName("thing6");
                data.setValue("您参与的抽奖已经开奖，点击查看中奖名单～");
                dataList.add(data);
                // 开奖时间
                data = new WxMaSubscribeMessage.Data();
                data.setName("date8");
                data.setValue(DateUtil.format(now, DatePattern.NORM_DATETIME_PATTERN));
                dataList.add(data);
                // 领取方式
                data = new WxMaSubscribeMessage.Data();
                data.setName("thing10");
                data.setValue("请前往小程序领取");
                dataList.add(data);
                WxMaSubscribeMessage wxMaSubscribeMessage = WxMaSubscribeMessage
                        .builder()
                        .templateId(templateId)
                        .page(subscribeMessagePage + "?lotteryActivityId=" + lotteryActivity.getId())
                        .data(dataList)
                        .build();
                enableUserThirdNoticeList.parallelStream()
                        .forEach(userThirdNotice -> {
                            sendSubscribeMessage(wxMaSubscribeMessage, userThirdNotice.getOpenId());
                            baseUserThirdNoticeService.updateUserThirdNoticeEnableSendNum(wxMaSubscribeMessage.getTemplateId(), miniAppAppId, userThirdNotice.getUserId(), CommonConstant.DEFAULT_LONG_NEGATIVE_1);
                        });
            } catch (Exception e) {
                log.error("LotteryOpenDelayHandler-sendWATemplate Exception", e);
            }
        }
    }

    @Override
    @Async
    public void sendWATemplate(Long userId, WxMaSubscribeMessage wxMaSubscribeMessage) {
        Assert.notNull(userId, "userId");
        Assert.notNull(wxMaSubscribeMessage, "wxMaSubscribeMessage");

        UserThirdNotice userThirdNotice = baseUserThirdNoticeService.getEnableUserThirdNotice(wxMaSubscribeMessage.getTemplateId(), miniAppAppId, userId);
        if (Objects.isNull(userThirdNotice)) {
            log.debug("BaseWxServiceImpl-sendWATemplate userThirdNotice is null userId: {}", userId);
            return;
        }
        sendSubscribeMessage(wxMaSubscribeMessage, userThirdNotice.getOpenId());
        baseUserThirdNoticeService.updateUserThirdNoticeEnableSendNum(wxMaSubscribeMessage.getTemplateId(), miniAppAppId, userId, CommonConstant.DEFAULT_LONG_NEGATIVE_1);
    }

}
