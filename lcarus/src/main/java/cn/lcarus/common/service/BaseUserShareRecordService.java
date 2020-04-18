package cn.lcarus.common.service;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.lcarus.common.entity.UserShareRecord;
import cn.lcarus.guess.web.param.LoginUserVo;
import cn.lcarus.guess.web.param.UserShareDrawParam;
import cn.lcarus.guess.web.res.UserShareDrawRes;
import cn.lcarus.guess.web.res.UserShareResultRes;

/**
 * 用户分享记录表 服务类
 *
 * @author xingcheng
 * @since 2020-03-04
 */
public interface BaseUserShareRecordService extends BaseService<UserShareRecord> {

    /**
     * 分享结果回调
     *
     * @param param
     * @return
     */
    @Deprecated
    ApiResult<UserShareResultRes> shareResult(LoginUserVo param);

    /**
     * 分享结果回调后领取红包
     *
     * @param param
     * @return
     */
    @Deprecated
    ApiResult<UserShareDrawRes> draw(UserShareDrawParam param);

    /**
     * 根据活动id获取活动记录
     *
     * @param userShareRecordId
     * @return
     */
    UserShareRecord getUserShareRecord(Long userShareRecordId);

    /**
     * 查询用户某天分享记录数
     *
     * @param userId
     * @param shareDay
     * @return
     */
    Integer countUserShareRecord(Long userId, String shareDay);

    /**
     * 是否有效
     *
     * @param userShareRecord
     * @return
     */
    boolean isValid(UserShareRecord userShareRecord);

    /**
     * 用户分享结果回调接口
     *
     * @param param
     * @return
     */
    ApiResult<UserShareResultRes> shareResultCallback(LoginUserVo param);

    /**
     * 用户分享结果回调后领取红包接口
     *
     * @param param
     * @return
     */
    ApiResult<UserShareDrawRes> drawCall(LoginUserVo param);
}
