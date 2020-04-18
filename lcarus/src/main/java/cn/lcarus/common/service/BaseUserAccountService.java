package cn.lcarus.common.service;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.lcarus.common.entity.UserAccount;
import cn.lcarus.common.entity.UserAccountRecord;
import cn.lcarus.guess.web.param.LoginUserPageVo;
import cn.lcarus.guess.web.param.LoginUserVo;
import cn.lcarus.guess.web.param.UserWithDrewParam;
import cn.lcarus.guess.web.res.UserAccountInfoRes;
import cn.lcarus.guess.web.res.UserAccountRecordInfoRes;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;

/**
 * 用户账户 服务类
 *
 * @author xingcheng
 * @since 2020-03-04
 */
public interface BaseUserAccountService extends BaseService<UserAccount> {

    /**
     * 变更用户账户金额
     *
     * @param userId
     * @param amount
     * @param useType UserAccountUseTypeEnum
     */
    Long changeUserAccountAmount(Long userId, BigDecimal amount, Integer useType);

    /**
     * 获取最新的用户账户信息(新建事务)
     *
     * @param userId
     * @return
     */
    UserAccount getUserAccountNewEst(Long userId);

    /**
     * 获取用户账户信息（旧事务）
     *
     * @param userId
     * @return
     */
    UserAccount getUserAccount(Long userId);

    /**
     * 获取用户账户金额（旧事务）
     *
     * @param userId
     * @return
     */
    UserAccount getUserAccountAmount(Long userId);

    /**
     * 用户账户信息展示
     *
     * @param param
     * @return
     */
    ApiResult<UserAccountInfoRes> showInfo(LoginUserVo param);

    /**
     * 用户红包提现接口
     *
     * @param param
     * @return
     */
    ApiResult<?> cashWithdrawal(UserWithDrewParam param);

    /**
     * 查询用户收支明细
     *
     * @param param
     * @return
     */
    ApiResult<Page<UserAccountRecordInfoRes>> showRecord(LoginUserPageVo<UserAccountRecord> param);

    /**
     * 仅仅变更账户金额
     * @param userId
     * @param amount
     * @return
     */
    Long changeUserAccountAmount(Long userId, BigDecimal amount);
}
