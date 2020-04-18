package cn.lcarus.common.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.chinotan.zeus.config.constant.CacheKey;
import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.chinotan.zeus.framework.front.login.service.BaseLoginService;
import cn.chinotan.zeus.framework.util.LoginUtil;
import cn.chinotan.zeus.redislock.annotation.DistributeLock;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.lcarus.common.entity.User;
import cn.lcarus.common.entity.UserAccount;
import cn.lcarus.common.entity.UserThirdInfo;
import cn.lcarus.common.mapper.UserThirdInfoMapper;
import cn.lcarus.common.service.BaseUserAccountService;
import cn.lcarus.common.service.BaseUserService;
import cn.lcarus.common.service.BaseUserThirdInfoService;
import cn.lcarus.guess.web.param.*;
import cn.lcarus.guess.web.res.UserLoginRes;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户三方信息 服务都抽象类
 * </p>
 *
 * @author xingcheng
 * @since 2019-08-24
 */
@Slf4j
@Service
public class BaseUserThirdInfoServiceImpl extends BaseServiceImpl<UserThirdInfoMapper, UserThirdInfo> implements BaseUserThirdInfoService {

    @Autowired
    private BaseLoginService baseLoginService;

    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    @Lazy
    private BaseUserThirdInfoService baseUserThirdInfoService;

    @Autowired
    private WxMaUserService wxMaUserService;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private BaseUserAccountService baseUserAccountService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${wx.miniapp.appid}")
    private String miniAppAppId;

    @Value("${wx.mp.appId}")
    private String mpAppId;

    @Value("${lcarus.wx.miniapp.enable-check-signature}")
    private boolean enableCheckSignature;

    @Value("${zeus.login.front.token-valid-time}")
    private Integer tokenValidTime;

    @Override
    public ApiResult<String> grantLogin(UserGrantLoginParam param) throws WxErrorException {
        Assert.notNull(param, "param");
        // code
        String code = param.getCode();
        String appId = param.getAppId();
        Assert.notNull(ApiErrorCode.FAILED, code, appId);

        ApiResult<ThirdUserDetailParam> checkAndGetThirdUserDetailResult = checkAndGetThirdUserDetail(param, code, appId, null);
        if (checkAndGetThirdUserDetailResult.isFail() || checkAndGetThirdUserDetailResult.getData() == null) {
            return ApiResult.fail(checkAndGetThirdUserDetailResult);
        }
        // 更新或者保存用户并获取token
        UserLoginRes userLoginRes = baseUserThirdInfoService.getTokenAndSaveThirdUser(checkAndGetThirdUserDetailResult.getData());
        String token = userLoginRes.getToken();
        // 更新小程序用户sessionKey
        if (Objects.equals(appId, miniAppAppId)) {
            baseUserThirdInfoService.updateMiNiAppUserSessionKey(userLoginRes.getUserId(), userLoginRes.getAppId(), userLoginRes.getSessionKey());
        }
        return ApiResult.ok(token);
    }

    @Override
    @Async
    @Retryable(value = {Exception.class}, exclude = {ApiException.class}, maxAttempts = 5, backoff = @Backoff(delay = 2000L, multiplier = 3))
    public void updateMiNiAppUserSessionKey(Long userId, String appId, String sessionKey) {
        Assert.notNull(userId, "userId");
        Assert.notBlank(sessionKey, "sessionKey");
        redisTemplate.opsForValue().set(CacheKey.USER_SESSION_KEY + appId + StrUtil.COLON + userId, sessionKey, tokenValidTime, TimeUnit.MINUTES);
    }

    @Override
    public String getMiNiAppUserSessionKey(Long userId, String appId) {
        Assert.notNull(userId, "userId");
        Assert.notBlank(appId, "appId");
        return redisTemplate.opsForValue().get(CacheKey.USER_SESSION_KEY + appId + StrUtil.COLON + userId);
    }

    @Override
    @DistributeLock(value = "USER_THIRD_INFO:SYNC_DETAIL", key = "#param.userId", expire = 360, timeout = 0, errMsg = "正在同步中，请不要频繁点击哦！")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> syncThirdInfo(ThirdUserDetailSyncParam param) throws WxErrorException {
        Assert.notNull(param, "param");
        String appId = param.getAppId();
        Long userId = param.getUserId();
        Assert.notNull(ApiErrorCode.FAILED, appId, userId);

        ApiResult<ThirdUserDetailParam> checkAndGetThirdUserDetailResult = checkAndGetThirdUserDetail(param, null, appId, userId);
        if (checkAndGetThirdUserDetailResult.isFail() || checkAndGetThirdUserDetailResult.getData() == null) {
            return checkAndGetThirdUserDetailResult;
        }
        // 进行资料同步
        UserThirdInfo userThirdInfo = baseUserThirdInfoService.getUserThirdInfo(userId, appId);
        if (Objects.isNull(userThirdInfo)) {
            return ApiResult.fail(ApiCode.ERROR_6409);
        }
        ThirdUserDetailParam thirdUserDetailParam = checkAndGetThirdUserDetailResult.getData();
        if (!Objects.equals(userThirdInfo.getOpenId(), thirdUserDetailParam.getOpenId()) || !Objects.equals(userThirdInfo.getUnionId(), thirdUserDetailParam.getUnionId())) {
            return ApiResult.fail(ApiCode.ERROR_6410);
        }
        UserThirdInfo userThirdInfoUpdate = BeanUtil.toBean(thirdUserDetailParam, UserThirdInfo.class);
        userThirdInfoUpdate.setId(userThirdInfo.getId());
        userThirdInfoUpdate.setUpdateTime(DateUtil.date());
        boolean update = updateById(userThirdInfoUpdate);
        Assert.isTrue(update);
        return ApiResult.ok();
    }

    @Override
    public ApiResult<?> syncThirdSession(UserSyncThirdSessionParam param) throws WxErrorException {
        String appId = param.getAppId();
        String code = param.getCode();
        Long userId = param.getUserId();
        Assert.notNull(ApiErrorCode.FAILED, appId, userId, code);
        // 获取三方session
        String sessionKey = null;
        if (Objects.equals(appId, miniAppAppId)) {
            WxMaJscode2SessionResult sessionInfo = wxMaUserService.getSessionInfo(code);
            if (Objects.isNull(sessionInfo)) {
                return ApiResult.fail(ApiCode.ERROR_6402);
            }
            sessionKey = sessionInfo.getSessionKey();
        }
        if (StrUtil.isNotBlank(sessionKey)) {
            baseUserThirdInfoService.updateMiNiAppUserSessionKey(userId, appId, sessionKey);
        }
        return ApiResult.ok();
    }

    private ApiResult<ThirdUserDetailParam> checkAndGetThirdUserDetail(UseInfoBaseParam param, String code, String appId, Long userId) throws WxErrorException {
        ThirdUserDetailParam thirdUserDetailParam = new ThirdUserDetailParam();
        thirdUserDetailParam.setAppId(appId);
        if (Objects.equals(appId, miniAppAppId)) {
            // 小程序
            String sessionKey = null;
            if (StrUtil.isBlank(code) && Objects.nonNull(userId)) {
                sessionKey = getMiNiAppUserSessionKey(userId, appId);
            } else if (StrUtil.isNotBlank(code)) {
                WxMaJscode2SessionResult sessionInfo = wxMaUserService.getSessionInfo(code);
                if (Objects.isNull(sessionInfo)) {
                    return ApiResult.fail(ApiCode.ERROR_6402);
                }
                sessionKey = sessionInfo.getSessionKey();
            }

            // 获取明细
            if (StrUtil.isBlank(sessionKey) || StrUtil.isBlank(param.getIv()) || StrUtil.isBlank(param.getEncryptedData())) {
                log.error("BaseUserThirdInfoServiceImpl-checkAndGetThirdUserDetail sessionKey or iv or encryptedData is null param: {}, code: {}, appId: {}, userId: {}, sessionKey: {}"
                        , JSON.toJSONString(param), code, appId, userId, sessionKey);
                return ApiResult.fail(ApiCode.ERROR_6407);
            }
            if (enableCheckSignature) {
                if (StrUtil.isBlank(param.getSignature()) || StrUtil.isBlank(param.getRawData())) {
                    log.error("BaseUserThirdInfoServiceImpl-checkAndGetThirdUserDetail signature or encryptedData is null param: {}, code: {}, appId: {}, userId: {}, sessionKey: {}"
                            , JSON.toJSONString(param), code, appId, userId, sessionKey);
                    return ApiResult.fail(ApiCode.ERROR_6415);
                }
                boolean checkUserInfo = wxMaUserService.checkUserInfo(sessionKey, param.getRawData(), param.getSignature());
                if (!checkUserInfo) {
                    log.error("BaseUserThirdInfoServiceImpl-checkAndGetThirdUserDetail checkUserInfo is fail param: {}, code: {}, appId: {}, userId: {}, sessionKey: {}"
                            , JSON.toJSONString(param), code, appId, userId, sessionKey);
                    return ApiResult.fail(ApiCode.ERROR_6413);
                }
            }
            WxMaUserInfo wxMaUserInfo = wxMaUserService.getUserInfo(sessionKey, param.getEncryptedData(), param.getIv());
            if (StrUtil.isBlank(wxMaUserInfo.getUnionId())) {
                log.error("BaseUserThirdInfoServiceImpl-checkAndGetThirdUserDetail unionId is null param: {}, code: {}, appId: {}, userId: {}, sessionKey: {}, wxMaUserInfo: {}"
                        , JSON.toJSONString(param), code, appId, userId, sessionKey, JSON.toJSONString(wxMaUserInfo));
                return ApiResult.fail(ApiCode.ERROR_6406);
            }
            BeanUtil.copyProperties(wxMaUserInfo, thirdUserDetailParam);
            thirdUserDetailParam.setSessionKey(sessionKey);
        } else if (Objects.equals(appId, mpAppId)) {
            if (StrUtil.isBlank(code)) {
                log.error("BaseUserThirdInfoServiceImpl-checkAndGetThirdUserDetail code is null param: {}, code: {}, appId: {}, userId: {}"
                        , JSON.toJSONString(param), code, appId, userId);
                return ApiResult.fail(ApiCode.ERROR_6414);
            }
            // 公众号
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            if (Objects.isNull(wxMpOAuth2AccessToken)) {
                log.error("BaseUserThirdInfoServiceImpl-checkAndGetThirdUserDetail wxMpOAuth2AccessToken is null param: {}, code: {}, appId: {}, userId: {}"
                        , JSON.toJSONString(param), code, appId, userId);
                return ApiResult.fail(ApiCode.ERROR_6402);
            }
            if (StrUtil.isBlank(wxMpOAuth2AccessToken.getUnionId())) {
                log.error("BaseUserThirdInfoServiceImpl-checkAndGetThirdUserDetail unionId is null param: {}, code: {}, appId: {}, userId: {}, wxMpOAuth2AccessToken: {}"
                        , JSON.toJSONString(param), code, appId, userId, JSON.toJSONString(wxMpOAuth2AccessToken));
                return ApiResult.fail(ApiCode.ERROR_6405);
            }
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            BeanUtil.copyProperties(wxMpUser, thirdUserDetailParam);
            thirdUserDetailParam.setNickName(wxMpUser.getNickname());
            thirdUserDetailParam.setAvatarUrl(wxMpUser.getHeadImgUrl());
            thirdUserDetailParam.setGender(wxMpUser.getSex() == null ? null : wxMpUser.getSex().toString());
        } else {
            return ApiResult.fail(ApiCode.ERROR_6401);
        }
        return ApiResult.ok(thirdUserDetailParam);
    }

    /**
     * 获取token并且保存三方用户信息
     *
     * @param param
     * @return
     */
    @Override
    @DistributeLock(value = "USER_THIRD_INFO:SAVE", key = "#param.unionId", expire = 360, timeout = 0, errMsg = "正在登陆中，请不要频繁点击哦！")
    @Transactional(rollbackFor = Exception.class)
    public UserLoginRes getTokenAndSaveThirdUser(ThirdUserDetailParam param) {
        String appId = param.getAppId();
        String openId = param.getOpenId();
        String unionId = param.getUnionId();
        DateTime now = DateUtil.date();
        Long userId;
        UserLoginRes userLoginRes = new UserLoginRes();
        userLoginRes.setSessionKey(param.getSessionKey());
        // 根据unionId获取userId
        List<UserThirdInfo> userThirdInfoList = getUserThirdInfo(unionId);
        if (CollUtil.isNotEmpty(userThirdInfoList)) {
            Optional<UserThirdInfo> firstUserThirdInfoOptional = userThirdInfoList
                    .stream()
                    .filter(v -> Objects.equals(v.getAppId(), appId) && Objects.equals(v.getOpenId(), openId))
                    .findFirst();
            if (firstUserThirdInfoOptional.isPresent()) {
                UserThirdInfo currentUserThirdInfo = firstUserThirdInfoOptional.get();
                // 进行更新
                UserThirdInfo userThirdInfoUpdate = BeanUtil.toBean(param, UserThirdInfo.class);
                userThirdInfoUpdate.setId(currentUserThirdInfo.getId());
                userThirdInfoUpdate.setUpdateTime(now);
                boolean update = updateById(userThirdInfoUpdate);
                Assert.isTrue(update);
                userId = currentUserThirdInfo.getUserId();
            } else {
                // 用老的userId
                userId = userThirdInfoList.get(0).getUserId();
                // 进行注册
                saveUserThirdInfo(param, userId, now);
            }
        } else {
            // 进行注册
            User user = new User();
            user.setCreateTime(now);
            user.setUpdateTime(now);
            baseUserService.save(user);
            userId = user.getId();
            saveUserThirdInfo(param, userId, now);
            // 用户开户
            UserAccount userAccountSave = new UserAccount();
            userAccountSave.setAmount(BigDecimal.ZERO);
            userAccountSave.setCreateTime(now);
            userAccountSave.setUpdateTime(userAccountSave.getCreateTime());
            userAccountSave.setUserId(userId);
            boolean saveUserAccount = baseUserAccountService.save(userAccountSave);
            Assert.isTrue(saveUserAccount);
        }
        // 生成token
        userLoginRes.setToken(baseLoginService.login(userId));
        userLoginRes.setUserId(userId);
        userLoginRes.setAppId(appId);
        return userLoginRes;
    }

    private void saveUserThirdInfo(ThirdUserDetailParam param, Long userId, DateTime now) {
        UserThirdInfo userThirdInfoSave = BeanUtil.toBean(param, UserThirdInfo.class);
        userThirdInfoSave.setUserId(userId);
        userThirdInfoSave.setCreateTime(now);
        userThirdInfoSave.setUpdateTime(now);
        boolean saveUser = save(userThirdInfoSave);
        Assert.isTrue(saveUser);
    }

    @Override
    public UserThirdInfo getUserThirdInfo(String openId, String appId) {
        Assert.notNull(ApiErrorCode.FAILED, openId, appId);
        return lambdaQuery()
                .eq(UserThirdInfo::getOpenId, openId)
                .eq(UserThirdInfo::getAppId, appId)
                .one();
    }

    @Override
    public UserThirdInfo getUserThirdInfo(Long userId, String appId) {
        Assert.notNull(userId, "userId");
        Assert.notBlank(appId, "appId");
        return lambdaQuery()
                .eq(UserThirdInfo::getUserId, userId)
                .eq(UserThirdInfo::getAppId, appId)
                .one();
    }

    @Override
    public List<UserThirdInfo> getUserThirdInfo(String unionId) {
        Assert.notNull(ApiErrorCode.FAILED, unionId);
        List<UserThirdInfo> list = lambdaQuery()
                .eq(UserThirdInfo::getUnionId, unionId)
                .list();
        return list;
    }

    @Override
    public List<UserThirdInfo> getUserThirdInfo(Collection<Long> userIds, String... appIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        String appId = null;
        if (appIds == null || appIds.length == 0) {
            appId = miniAppAppId;
        } else if (appIds.length == 1) {
            appId = appIds[0];
        }
        return lambdaQuery()
                .in(UserThirdInfo::getUserId, userIds)
                .eq(Objects.nonNull(appId), UserThirdInfo::getAppId, appId)
                .in(Objects.isNull(appId), UserThirdInfo::getAppId, appIds)
                .list();
    }

    @Override
    public Set<String> getUserThirdInfoList(Collection<Long> userIds, String appId) {
        Assert.notNull(appId);
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptySet();
        }
        List<UserThirdInfo> list = lambdaQuery()
                .in(UserThirdInfo::getUserId, userIds)
                .eq(UserThirdInfo::getAppId, appId)
                .select(UserThirdInfo::getOpenId)
                .list();
        if (CollUtil.isEmpty(list)) {
            return Collections.emptySet();
        }
        return list.stream()
                .map(UserThirdInfo::getOpenId)
                .collect(Collectors.toSet());
    }
}
