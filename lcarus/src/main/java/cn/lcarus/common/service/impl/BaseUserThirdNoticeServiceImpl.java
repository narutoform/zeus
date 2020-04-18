package cn.lcarus.common.service.impl;

import cn.chinotan.zeus.config.constant.CommonConstant;
import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.chinotan.zeus.framework.util.ValidatorUtils;
import cn.chinotan.zeus.redislock.annotation.DistributeLock;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.lcarus.common.entity.UserThirdInfo;
import cn.lcarus.common.entity.UserThirdNotice;
import cn.lcarus.common.enums.UserThirdNoticeStateEnum;
import cn.lcarus.common.mapper.UserThirdNoticeMapper;
import cn.lcarus.common.service.BaseUserThirdInfoService;
import cn.lcarus.common.service.BaseUserThirdNoticeService;
import cn.lcarus.guess.web.param.SubscribeResultParam;
import cn.lcarus.guess.web.param.UserThirdSubscribeParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 用户三方订阅消息记录 服务实现类
 *
 * @author xingcheng
 * @since 2020-03-14
 */
@Service
@Slf4j
public class BaseUserThirdNoticeServiceImpl extends BaseServiceImpl<UserThirdNoticeMapper, UserThirdNotice> implements BaseUserThirdNoticeService {

    @Autowired
    @Lazy
    private BaseUserThirdInfoService baseUserThirdInfoService;

    @Autowired
    @Lazy
    private BaseUserThirdNoticeService baseUserThirdNoticeService;

    @Value("${wx.miniapp.appid}")
    private String miniAppAppId;

    @Override
    @DistributeLock(value = "USER_THIRD_NOTICE:SUBSCRIBE_RESULT", key = "#param.userId + ':' + #param.appId", expire = 360, timeout = 0, errMsg = "订阅结果处理中，请不要频繁点击哦！")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> subscribeResult(UserThirdSubscribeParam param) {
        ValidatorUtils.validateModel(param);
        String appId = param.getAppId();
        if (!Objects.equals(appId, miniAppAppId)) {
            return ApiResult.fail(ApiCode.ERROR_6404);
        }
        Long userId = param.getUserId();
        DateTime now = DateUtil.date();
        Assert.notNull(userId, "userId");
        // 查询openId
        UserThirdInfo userThirdInfo = baseUserThirdInfoService.getUserThirdInfo(userId, appId);
        Assert.notNull(userThirdInfo, "userThirdInfo");
        List<SubscribeResultParam> subscribeResultList = param.getSubscribeResultList();
        if (CollUtil.isNotEmpty(subscribeResultList)) {
            subscribeResultList
                    .forEach(v -> {
                        if (Objects.equals(v.getState(), UserThirdNoticeStateEnum.ACCEPT.getCode())) {
                            UserThirdNotice userThirdNotice = getUserThirdNoticeNewEst(v.getTemplateId(), appId, userId);
                            if (Objects.nonNull(userThirdNotice)) {
                                // 更新
                                baseUserThirdNoticeService.updateUserThirdNoticeEnableSendNum(userThirdNotice.getTemplateId(), userThirdNotice.getAppId(), userThirdNotice.getUserId(), CommonConstant.DEFAULT_LONG_1);
                            } else {
                                // 保存
                                UserThirdNotice userThirdNoticeSave = BeanUtil.toBean(v, UserThirdNotice.class);
                                userThirdNoticeSave.setUserId(userId);
                                userThirdNoticeSave.setOpenId(userThirdInfo.getOpenId());
                                userThirdNoticeSave.setAppId(appId);
                                userThirdNoticeSave.setCreateTime(now);
                                userThirdNoticeSave.setUpdateTime(now);
                                userThirdNoticeSave.setEnableSendNum(CommonConstant.DEFAULT_LONG_1);
                                boolean save = save(userThirdNoticeSave);
                                Assert.isTrue(save);
                            }
                        }
                    });
        }
        return ApiResult.ok();
    }

    @Override
    public UserThirdNotice getUserThirdAcceptNotice(String templateId, String appId, Long userId) {
        Assert.notNull(userId, "userId");
        Assert.notBlank(appId, "appId");
        Assert.notBlank(templateId, "templateId");

        return lambdaQuery()
                .eq(UserThirdNotice::getTemplateId, templateId)
                .eq(UserThirdNotice::getAppId, appId)
                .eq(UserThirdNotice::getUserId, userId)
                .gt(UserThirdNotice::getEnableSendNum, CommonConstant.DEFAULT_LONG_ZERO)
                .one();
    }

    @Override
    public UserThirdNotice getUserThirdNoticeNewEst(String templateId, String appId, Long userId) {
        Assert.notNull(userId, "userId");
        Assert.notBlank(appId, "appId");
        Assert.notBlank(templateId, "templateId");

        return lambdaQuery()
                .eq(UserThirdNotice::getTemplateId, templateId)
                .eq(UserThirdNotice::getAppId, appId)
                .eq(UserThirdNotice::getUserId, userId)
                .one();
    }

    @Override
    public boolean existUserThirdNotice(String templateId, String appId, Long userId) {
        Assert.notNull(userId, "userId");
        Assert.notBlank(appId, "appId");
        Assert.notBlank(templateId, "templateId");

        return lambdaQuery()
                .eq(UserThirdNotice::getTemplateId, templateId)
                .eq(UserThirdNotice::getAppId, appId)
                .eq(UserThirdNotice::getUserId, userId)
                .count() > 0;
    }

    @Override
    public List<UserThirdNotice> getEnableUserThirdNoticeList(Collection<Long> userIds, String templateId, String appId) {
        Assert.notNull(appId, "appId");
        Assert.notBlank(templateId, "templateId");
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return lambdaQuery()
                .eq(UserThirdNotice::getTemplateId, templateId)
                .eq(UserThirdNotice::getAppId, appId)
                .in(UserThirdNotice::getUserId, userIds)
                .gt(UserThirdNotice::getEnableSendNum, CommonConstant.DEFAULT_LONG_ZERO)
                .select(UserThirdNotice::getOpenId, UserThirdNotice::getUserId)
                .list();
    }

    @Override
    public UserThirdNotice getEnableUserThirdNotice(String templateId, String appId, Long userId) {
        Assert.notNull(userId, "userId");
        Assert.notBlank(appId, "appId");
        Assert.notBlank(templateId, "templateId");

        return lambdaQuery()
                .eq(UserThirdNotice::getTemplateId, templateId)
                .eq(UserThirdNotice::getAppId, appId)
                .eq(UserThirdNotice::getUserId, userId)
                .gt(UserThirdNotice::getEnableSendNum, CommonConstant.DEFAULT_LONG_ZERO)
                .one();
    }


    @Override
    @Retryable(value = {Exception.class}, maxAttempts = 5, backoff = @Backoff(delay = 2000L, multiplier = 3))
    @Transactional(rollbackFor = Exception.class)
    public void updateUserThirdNoticeEnableSendNum(String templateId, String appId, Long userId, Long offsetNum) {
        Assert.notNull(userId, "userId");
        Assert.notBlank(appId, "appId");
        Assert.notBlank(templateId, "templateId");
        Assert.notNull(offsetNum, "offsetNum");

        UserThirdNotice userThirdNoticeNewEst = baseUserThirdNoticeService.getUserThirdNoticeNewEst(templateId, appId, userId);
        if (Objects.isNull(userThirdNoticeNewEst)) {
            log.error("UserThirdNoticeServiceImpl-updateUserThirdNoticeEnableSendNum userThirdNoticeNewEst is null templateId: {}, appId: {}, userId: {}, offsetNum: {}", templateId, appId, userId, offsetNum);
            return;
        }
        if (userThirdNoticeNewEst.getEnableSendNum() + offsetNum < 0L) {
            log.error("UserThirdNoticeServiceImpl-updateUserThirdNoticeEnableSendNum getEnableSendNum is equal zero and offsetNum lt zero templateId: {}, appId: {}, userId: {}, offsetNum: {}", templateId, appId, userId, offsetNum);
            return;
        }
        UserThirdNotice userThirdNoticeUpdate = new UserThirdNotice();
        userThirdNoticeUpdate.setId(userThirdNoticeNewEst.getId());
        userThirdNoticeUpdate.setVersion(userThirdNoticeNewEst.getVersion());
        userThirdNoticeUpdate.setEnableSendNum(userThirdNoticeNewEst.getEnableSendNum() + offsetNum);
        userThirdNoticeUpdate.setUpdateTime(DateUtil.date());
        boolean update = updateById(userThirdNoticeUpdate);
        Assert.isTrue(update);
    }
}
