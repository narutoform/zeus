package cn.lcarus.common.service;

import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.hutool.core.date.DateTime;
import cn.lcarus.common.entity.LotteryActivity;
import cn.lcarus.common.entity.UserAccountRecord;
import com.github.binarywang.wxpay.exception.WxPayException;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @program: guess
 * @description: 微信接口封装
 * @author: xingcheng
 * @create: 2019-09-01 20:14
 **/
public interface BaseWxService {

    /**
     * 商户API证书校验出错 请求没带商户API证书或者带上了错误的商户API证书 您使用的调用证书有误，请确认是否使用了正确的证书，可以前往商户平台重新下载，证书需与商户号对应，如果要继续付款必须使用原商户订单号重试。
     */
    String CA_ERROR = "CA_ERROR";

    /**
     * 请求已受理，请稍后使用原单号查询发放结果 系统无返回明确发放结果 使用原单号调用接口，查询发放结果，如果使用新单号调用接口，视为新发放请求
     */
    String SYSTEM_ERROR = "SYSTEMERROR";

    /**
     * 请求已受理，请稍后使用原单号查询发放结果 红包正在发放中 请在红包发放二十分钟后查询,按照查询结果成功失败进行处理，如果依然是PROCESSING的状态，请使用原商户订单号重试
     */
    String PROCESSING = "PROCESSING";

    /**
     * 红包发放失败,请更换单号再重试 该红包已经发放失败 如果需要重新发放，请更换单号再发放
     */
    String SEND_FAILED = "SEND_FAILED";

    Integer DEFAULT_SIZE = 5000;

    /**
     * 发送现金
     *
     * @param userId
     * @param amount
     * @param appId
     * @param userAccountRecordId
     * @return
     */
    ApiResult<?> sendRealAmount(Long userId, BigDecimal amount, String appId, Long userAccountRecordId);

    /**
     * 发送微信小程序模板消息
     *
     * @param userId
     * @param wxMaSubscribeMessage
     */
    void sendWATemplate(Long userId, WxMaSubscribeMessage wxMaSubscribeMessage);

    /**
     * 发送提现成功通知
     *
     * @param userId
     * @param amount
     */
    void sendWithDrawSuccessNotice(Long userId, BigDecimal amount);

    /**
     * 发送微信小程序模板消息（全量发送）
     * @param wxMaSubscribeMessage
     */
    void sendAllUserThirdNoticeWATemplate(WxMaSubscribeMessage wxMaSubscribeMessage);

    /**
     * 重洗发起提现
     * @param userAccountRecord
     */
    void reSendRealAmount(UserAccountRecord userAccountRecord) throws WxPayException;

    /**
     * 发送开奖结果通知
     * @param lotteryActivity
     * @param now
     * @param waitSendNoticeUserIdSet
     */
    void sendOpenLotteryNotice(LotteryActivity lotteryActivity, DateTime now, Set<Long> waitSendNoticeUserIdSet);

}
