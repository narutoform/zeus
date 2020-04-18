package cn.lcarus.common.service;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.lcarus.common.entity.UserThirdInfo;
import cn.lcarus.guess.web.param.ThirdUserDetailParam;
import cn.lcarus.guess.web.param.ThirdUserDetailSyncParam;
import cn.lcarus.guess.web.param.UserGrantLoginParam;
import cn.lcarus.guess.web.param.UserSyncThirdSessionParam;
import cn.lcarus.guess.web.res.UserLoginRes;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户三方信息 服务类
 * </p>
 *
 * @author xingcheng
 * @since 2019-08-24
 */
public interface BaseUserThirdInfoService extends BaseService<UserThirdInfo> {

    /**
     * 授权登录获取token
     *
     * @param param
     * @return
     * @throws WxErrorException
     */
    ApiResult<String> grantLogin(UserGrantLoginParam param) throws WxErrorException;

    /**
     * 获取token，并且保存三方用户信息
     *
     * @param thirdUserDetailParam
     * @return
     */
    UserLoginRes getTokenAndSaveThirdUser(ThirdUserDetailParam thirdUserDetailParam);

    /**
     * 更新小程序sessionKey
     *
     * @param userId
     * @param appId
     * @param sessionKey
     */
    void updateMiNiAppUserSessionKey(Long userId, String appId, String sessionKey);

    /**
     * 获取小程序sessionKey
     *
     * @param userId
     * @param appId
     */
    String getMiNiAppUserSessionKey(Long userId, String appId);

    /**
     * 根据用户id和openId和appId获取唯一关联信息
     *
     * @param openId
     * @param appId
     * @return
     */
    UserThirdInfo getUserThirdInfo(String openId, String appId);

    /**
     * 通过用户id和appId获取用户关联信息
     *
     * @param userId
     * @param appId
     * @return
     */
    UserThirdInfo getUserThirdInfo(Long userId, String appId);

    /**
     * 根据unionId获取三方用户信息
     *
     * @param unionId
     * @return
     */
    List<UserThirdInfo> getUserThirdInfo(String unionId);

    /**
     * 批量获取三方用户信息
     *
     * @param userIds
     * @param appIds
     * @return
     */
    List<UserThirdInfo> getUserThirdInfo(Collection<Long> userIds, String... appIds);

    /**
     * 同步资料
     *
     * @param param
     * @return
     * @throws WxErrorException
     */
    ApiResult<?> syncThirdInfo(ThirdUserDetailSyncParam param) throws WxErrorException;

    /**
     * 登录同步三方session接口
     *
     * @param param
     * @return
     */
    ApiResult<?> syncThirdSession(UserSyncThirdSessionParam param) throws WxErrorException;

    /**
     * 批量获取用户的openId
     * @param userIds
     * @param appId
     * @return
     */
    Set<String> getUserThirdInfoList(Collection<Long> userIds, String appId);
}
