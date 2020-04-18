package cn.lcarus.common.service.impl;

import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.lcarus.common.entity.UserAccountRecord;
import cn.lcarus.common.enums.UserAccountRecordStateEnum;
import cn.lcarus.common.enums.UserAccountTypeEnum;
import cn.lcarus.common.enums.UserAccountUseTypeEnum;
import cn.lcarus.common.mapper.UserAccountRecordMapper;
import cn.lcarus.common.service.BaseUserAccountRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 账户明细 服务实现类
 *
 * @author xingcheng
 * @since 2020-03-04
 */
@Service
@Slf4j
public class BaseUserAccountRecordServiceImpl extends BaseServiceImpl<UserAccountRecordMapper, UserAccountRecord> implements BaseUserAccountRecordService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveUserAccountRecord(Long userId, BigDecimal amount, Integer useType, DateTime now) {
        Assert.isTrue(amount.compareTo(BigDecimal.ZERO) != 0);
        UserAccountRecord userAccountRecordSave = new UserAccountRecord();
        userAccountRecordSave.setUserId(userId);
        userAccountRecordSave.setAmount(amount.abs());
        userAccountRecordSave.setCreateTime(now);
        userAccountRecordSave.setUpdateTime(userAccountRecordSave.getCreateTime());
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            // 支出
            userAccountRecordSave.setType(UserAccountTypeEnum.OUT.getCode());
        } else if (amount.compareTo(BigDecimal.ZERO) > 0) {
            // 支入
            userAccountRecordSave.setType(UserAccountTypeEnum.IN.getCode());
        }
        userAccountRecordSave.setUseType(useType);
        if (Objects.equals(useType, UserAccountUseTypeEnum.RED_PACK.getCode())) {
            userAccountRecordSave.setState(UserAccountRecordStateEnum.SUCCESS.getCode());
        } else {
            userAccountRecordSave.setState(UserAccountRecordStateEnum.INIT.getCode());
        }
        boolean saveUserAccountRecord = save(userAccountRecordSave);
        Assert.isTrue(saveUserAccountRecord);
        return userAccountRecordSave.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAccountRecordState(Long userAccountRecordId, Integer state, Integer payChannel, String errMsg, String thirdUniqueId) {
        updateUserAccountRecordStatePrivate(userAccountRecordId, state, payChannel, errMsg, thirdUniqueId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAccountRecordState(Long userAccountRecordId, Integer state, Integer payChannel, String errMsg) {
        updateUserAccountRecordStatePrivate(userAccountRecordId, state, payChannel, errMsg, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAccountRecordStateSuccess(Long userAccountRecordId, Integer payChannel, String thirdUniqueId) {
        updateUserAccountRecordStatePrivate(userAccountRecordId, UserAccountRecordStateEnum.SUCCESS.getCode(), payChannel, null, thirdUniqueId);
    }

    private void updateUserAccountRecordStatePrivate(Long userAccountRecordId, Integer state, Integer payChannel, String errMsg, String thirdUniqueId) {
        Assert.notNull(userAccountRecordId, "userAccountRecordId");
        Assert.notNull(state, "state");
        Assert.notNull(payChannel, "payChannel");

        // 查询
        UserAccountRecord userAccountRecord = getById(userAccountRecordId);
        Assert.notNull(userAccountRecord, "userAccountRecord");

        UserAccountRecord userAccountRecordUpdate = new UserAccountRecord();
        userAccountRecordUpdate.setId(userAccountRecordId);
        userAccountRecordUpdate.setUpdateTime(DateUtil.date());
        userAccountRecordUpdate.setState(state);
        userAccountRecordUpdate.setPayChannel(payChannel);
        userAccountRecordUpdate.setErrMsg(errMsg);
        userAccountRecordUpdate.setThirdUniqueId(thirdUniqueId);
        boolean updateById = updateById(userAccountRecordUpdate);
        Assert.isTrue(updateById);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void updateUserAccountRecordFinalFail(Long userAccountRecordId, Integer payChannel, String errMsg) {
        updateUserAccountRecordStatePrivate(userAccountRecordId, UserAccountRecordStateEnum.FINAL_FAIL.getCode(), payChannel, errMsg, null);
    }

    @Override
    public BigDecimal getUserWithDrewAccountAmount(Long userId) {
        Assert.notNull(userId, "userId");

        List<UserAccountRecord> list = lambdaQuery()
                .eq(UserAccountRecord::getUserId, userId)
                .eq(UserAccountRecord::getUseType, UserAccountUseTypeEnum.WITH_DREW.getCode())
                .select(UserAccountRecord::getAmount)
                .list();
        if (CollUtil.isEmpty(list)) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalWithDrewAmount = BigDecimal.ZERO;
        for (UserAccountRecord userAccountRecord : list) {
            totalWithDrewAmount = totalWithDrewAmount.add(userAccountRecord.getAmount());
        }
        return totalWithDrewAmount;
    }
}
