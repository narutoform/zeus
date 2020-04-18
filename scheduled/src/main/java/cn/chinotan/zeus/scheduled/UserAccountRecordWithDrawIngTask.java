package cn.chinotan.zeus.scheduled;

import cn.chinotan.zeus.config.constant.CommonConstant;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.lcarus.common.entity.UserAccountRecord;
import cn.lcarus.common.enums.UserAccountRecordPayChannelEnum;
import cn.lcarus.common.enums.UserAccountRecordStateEnum;
import cn.lcarus.common.service.BaseUserAccountRecordService;
import cn.lcarus.common.service.BaseWxService;
import com.alibaba.fastjson.JSON;
import com.github.binarywang.wxpay.bean.result.WxPayRedpackQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.RedpackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @program: lcarus
 * @description: 用户抽奖记录提现 进行中（可重试）定时补偿
 * @author: xingcheng
 * @create: 2020-03-14 17:56
 **/
@Slf4j
@Component
public class UserAccountRecordWithDrawIngTask {

    @Autowired
    private RedpackService redpackService;

    @Autowired
    private BaseWxService baseWxService;

    @Autowired
    private BaseUserAccountRecordService baseUserAccountRecordService;

    /**
     * 每5分支查询一次
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void userAccountRecordWithDrawIngTask() {
        List<UserAccountRecord> userAccountRecordList = baseUserAccountRecordService
                .lambdaQuery()
                .gt(UserAccountRecord::getCreateTime, DateUtil.offsetDay(DateUtil.date(), -1))
                .eq(UserAccountRecord::getState, UserAccountRecordStateEnum.ING.getCode())
                .list();
        if (CollUtil.isEmpty(userAccountRecordList)) {
            return;
        }
        for (UserAccountRecord userAccountRecord : userAccountRecordList) {
            // 查询红包发放记录
            Long mchBillNo = userAccountRecord.getId();
            WxPayRedpackQueryResult wxPayRedpackQueryResult = null;
            try {
                wxPayRedpackQueryResult = redpackService.queryRedpack(String.valueOf(mchBillNo));
            } catch (WxPayException e) {
                log.error("UserAccountRecordWithDrawIngTask-queryRedpack WxPayException userAccountRecord: {}", JSON.toJSONString(userAccountRecord), e);
            } catch (Exception e) {
                log.error("UserAccountRecordWithDrawIngTask-queryRedpack Exception userAccountRecord: {}", JSON.toJSONString(userAccountRecord), e);
            }
            try {
                if (Objects.isNull(wxPayRedpackQueryResult)) {
                    log.error("UserAccountRecordWithDrawIngTask-queryRedpack wxPayRedpackQueryResult is null userAccountRecord: {}", JSON.toJSONString(userAccountRecord));
                }
                assert wxPayRedpackQueryResult != null;
                String status = wxPayRedpackQueryResult.getStatus();
                switch (status) {
                    case CommonConstant.SENT:
                        // 发放成功
                        baseUserAccountRecordService.updateUserAccountRecordStateSuccess(mchBillNo, UserAccountRecordPayChannelEnum.WX_MP_RED.getCode(), wxPayRedpackQueryResult.getDetailId());
                        // 发送通知
                        baseWxService.sendWithDrawSuccessNotice(userAccountRecord.getUserId(), userAccountRecord.getAmount());
                        break;
                    default:
                        log.warn("UserAccountRecordWithDrawIngTask-not to deal status {}, userId {} userAccountRecord {}, wxPayRedpackQueryResult {}",
                                status, userAccountRecord.getUserId(), JSON.toJSONString(userAccountRecord), JSON.toJSONString(wxPayRedpackQueryResult));
                        break;
                }
            } catch (Exception e) {
                log.error("UserAccountRecordWithDrawIngTask-deal queryRedpack result Exception userAccountRecord: {}", JSON.toJSONString(userAccountRecord), e);
            }
        }
    }
}
