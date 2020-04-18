package cn.lcarus.common.service;

import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.hutool.core.date.DateTime;
import cn.lcarus.common.entity.UserAccountRecord;

import java.math.BigDecimal;

/**
 * 账户明细 服务类
 *
 * @author xingcheng
 * @since 2020-03-04
 */
public interface BaseUserAccountRecordService extends BaseService<UserAccountRecord> {

    /**
     * 保存账户变更记录
     *
     * @param userId
     * @param amount
     * @param useType
     * @param now
     * @return
     */
    Long saveUserAccountRecord(Long userId, BigDecimal amount, Integer useType, DateTime now);

    /**
     * 更新账户记录状态
     *
     * @param userAccountRecordId
     * @param state
     * @param payChannel
     * @param errMsg
     * @param thirdUniqueId
     */
    void updateUserAccountRecordState(Long userAccountRecordId, Integer state, Integer payChannel, String errMsg, String thirdUniqueId);

    /**
     * 更新账户记录状态
     *
     * @param userAccountRecordId
     * @param state
     * @param payChannel
     * @param errMsg
     */
    void updateUserAccountRecordState(Long userAccountRecordId, Integer state, Integer payChannel, String errMsg);

    /**
     * 更新账户记录状态(更新成功)
     *
     * @param userAccountRecordId
     * @param payChannel
     * @param thirdUniqueId
     */
    void updateUserAccountRecordStateSuccess(Long userAccountRecordId, Integer payChannel, String thirdUniqueId);

    /**
     * 更改用户账户记录状态最最终失败，且逻辑删除
     *
     * @param userAccountRecordId
     * @param payChannel
     * @param errMsg
     */
    void updateUserAccountRecordFinalFail(Long userAccountRecordId, Integer payChannel, String errMsg);

    /**
     * 获取用户已提现金额
     * @param userId
     * @return
     */
    BigDecimal getUserWithDrewAccountAmount(Long userId);

}
