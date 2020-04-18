package cn.lcarus.common.service;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.lcarus.common.entity.UserLotteryRecord;
import cn.lcarus.guess.web.param.LotteryActivityJoinParam;
import cn.lcarus.guess.web.param.UserAfterOpenDrawParam;
import cn.lcarus.guess.web.param.UserLotteryRecordParam;
import cn.lcarus.guess.web.res.LotteryActivityJoinRes;
import cn.lcarus.guess.web.res.UserLotteryRecordRes;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 用户抽奖记录表 服务类
 *
 * @author xingcheng
 * @since 2020-03-04
 */
public interface BaseUserLotteryRecordService extends BaseService<UserLotteryRecord> {

    String WIN_TYPE = "WIN";

    String JOIN_TYPE = "JOIN";

    /**
     * 根据活动id和用户id获取抽奖记录
     *
     * @param lotteryActivityId
     * @param userId
     * @return
     */
    UserLotteryRecord getUserLotteryRecord(Long lotteryActivityId, Long userId);

    /**
     * 获取并检测用户抽奖记录是否合法
     *
     * @param userLotteryRecordId
     * @param userId
     * @return
     */
    UserLotteryRecord getAndCheckUserLotteryRecord(Long userLotteryRecordId, Long userId);

    /**
     * 判断用户某个活动是否参与了
     *
     * @param lotteryActivityId
     * @param userId
     * @return
     */
    boolean existUserLotteryRecord(Long lotteryActivityId, Long userId);

    /**
     * 批量获取活动id和用户id的映射关系
     *
     * @param userId
     * @param lotteryActivityIds
     * @return
     */
    Set<Long> existUserLotteryRecord(Long userId, Collection<Long> lotteryActivityIds);

    /**
     * 用户是否参与过
     *
     * @param userId
     * @return
     */
    boolean existUserLotteryRecord(Long userId);

    /**
     * 获取参与人数
     *
     * @param lotteryActivityId
     * @return
     */
    Integer countJoin(Long lotteryActivityId);

    /**
     * 用户参与数
     *
     * @param userId
     * @return
     */
    Integer countUserJoin(Long userId);

    /**
     * 用户中奖数
     *
     * @param userId
     * @return
     */
    Integer countUserWin(Long userId);

    /**
     * 获取参与人列表
     *
     * @param lotteryActivityId
     * @param page
     * @return
     */
    Page<UserLotteryRecord> joinUserLotteryRecord(Long lotteryActivityId, Page<UserLotteryRecord> page);

    /**
     * 获取中奖人列表
     *
     * @param lotteryActivityId
     * @param page
     * @return
     */
    Page<UserLotteryRecord> winUserLotteryRecord(Long lotteryActivityId, Page<UserLotteryRecord> page);

    /**
     * 获取winType下的中奖人列表
     *
     * @param lotteryActivityId
     * @param page
     * @param winType           LotteryActivityTypeEnum
     * @return
     */
    Page<UserLotteryRecord> winUserLotteryRecord(Long lotteryActivityId, Page<UserLotteryRecord> page, Integer winType);

    /**
     * 当前中奖用户查询
     *
     * @param lotteryActivityId
     * @param userId
     * @return
     */
    UserLotteryRecord winCurrentUserLotteryRecord(Long lotteryActivityId, Long userId);

    /**
     * 展示用户抽奖记录
     *
     * @param param
     * @return
     */
    ApiResult<Page<UserLotteryRecordRes>> showRecordList(UserLotteryRecordParam param);

    /**
     * 参与抽奖
     *
     * @param param
     * @return
     */
    ApiResult<LotteryActivityJoinRes> join(LotteryActivityJoinParam param);

    /**
     * 开奖后用户确认领取接口
     *
     * @param param
     * @return
     */
    ApiResult<?> draw(UserAfterOpenDrawParam param);

    /**
     * 查询是否有效
     *
     * @param userLotteryRecord
     * @return
     */
    boolean isValid(UserLotteryRecord userLotteryRecord);

    /**
     * 查询所以的参与用户
     *
     * @param lotteryActivityId
     * @return
     */
    Integer countAllJoinNum(Long lotteryActivityId);

    /**
     * 查询所以的机器人参数数量
     * @param lotteryActivityId
     * @return
     */
    Integer countAllMachineJoinNum(Long lotteryActivityId);

    /**
     * 查询非机器人参数数量
     * @param lotteryActivityId
     * @return
     */
    Integer countAllHumanJoinNum(Long lotteryActivityId);

    /**
     * 查询中奖的记录（仅限制实物类奖品查询）
     * @param lotteryActivityId
     * @return
     */
    List<UserLotteryRecord> getWinHumanUserLotteryRecord(Long lotteryActivityId);

    /**
     * 获取全部参与用户
     * @param lotteryActivityId
     * @return
     */
    List<UserLotteryRecord> getJoinUserLotteryRecord(Long lotteryActivityId);

    /**
     * 获取全部真人参与记录
     * @param lotteryActivityId
     * @return
     */
    List<UserLotteryRecord> getJoinHumanUserLotteryRecord(Long lotteryActivityId);

    /**
     * 获取全部机器人参与记录
     * @param lotteryActivityId
     * @return
     */
    List<UserLotteryRecord> getJoinMachineUserLotteryRecord(Long lotteryActivityId);
}
