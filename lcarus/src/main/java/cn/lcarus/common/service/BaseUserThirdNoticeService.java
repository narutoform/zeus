package cn.lcarus.common.service;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.lcarus.common.entity.UserThirdNotice;
import cn.lcarus.guess.web.param.UserThirdSubscribeParam;

import java.util.Collection;
import java.util.List;

/**
 * 用户三方订阅消息记录 服务类
 *
 * @author xingcheng
 * @since 2020-03-14
 */
public interface BaseUserThirdNoticeService extends BaseService<UserThirdNotice> {

    /**
     * 订阅结果处理
     *
     * @param param
     * @return
     */
    ApiResult<?> subscribeResult(UserThirdSubscribeParam param);

    /**
     * 根据模块，用户id，appId获取已经订阅的通知
     *
     * @param templateId
     * @param appId
     * @param userId
     * @return
     */
    UserThirdNotice getUserThirdAcceptNotice(String templateId, String appId, Long userId);

    /**
     * 根据模块，用户id，appId获取的通知
     *
     * @param templateId
     * @param appId
     * @param userId
     * @return
     */
    UserThirdNotice getUserThirdNoticeNewEst(String templateId, String appId, Long userId);

    /**
     * 根据模块，用户id，appId判断是否订阅通知
     *
     * @param templateId
     * @param appId
     * @param userId
     * @return
     */
    boolean existUserThirdNotice(String templateId, String appId, Long userId);


    /**
     * 更新可用数量
     *
     * @param templateId
     * @param appId
     * @param userId
     * @param offsetNum
     */
    void updateUserThirdNoticeEnableSendNum(String templateId, String appId, Long userId, Long offsetNum);

    /**
     * 获取订阅通知的用户openId
     * @param userIds
     * @param templateId
     * @param appId
     * @return
     */
    List<UserThirdNotice> getEnableUserThirdNoticeList(Collection<Long> userIds, String templateId, String appId);

    /**
     * 根据模块，用户id，appId获取已经订阅的通知
     * @param templateId
     * @param appId
     * @param userId
     * @return
     */
    UserThirdNotice getEnableUserThirdNotice(String templateId, String appId, Long userId);

}
