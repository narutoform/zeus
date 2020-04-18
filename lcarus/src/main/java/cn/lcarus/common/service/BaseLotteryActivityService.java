package cn.lcarus.common.service;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.lcarus.admin.param.LotteryActivityAddParam;
import cn.lcarus.admin.param.LotteryActivityListParam;
import cn.lcarus.admin.res.LotteryActivityListVo;
import cn.lcarus.common.entity.LotteryActivity;
import cn.lcarus.guess.web.param.LoginUserPageVo;
import cn.lcarus.guess.web.param.LotteryActivityDetailParam;
import cn.lcarus.guess.web.param.LotteryJoinUserDetailParam;
import cn.lcarus.guess.web.param.LotteryWinUserDetailParam;
import cn.lcarus.guess.web.res.LotteryActivityDetailRes;
import cn.lcarus.guess.web.res.LotteryActivityIndexRes;
import cn.lcarus.guess.web.res.LotteryWinUserDetailRes;
import cn.lcarus.guess.web.res.UserShortInfoRes;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 抽奖活动 服务类
 *
 * @author xingcheng
 * @since 2020-03-04
 */
public interface BaseLotteryActivityService extends BaseService<LotteryActivity> {

    Integer DEFAULT_SIZE = 3;

    Integer DEFAULT_SIZE_TWO = 2;

    Integer DEFAULT_JOIN_SIZE = 16;

    /**
     * 首页展示
     *
     * @param param
     * @return
     */
    ApiResult<LotteryActivityIndexRes> showIndex(LoginUserPageVo param);

    /**
     * 抽奖活动明细
     *
     * @param param
     * @return
     */
    ApiResult<LotteryActivityDetailRes> showDetail(LotteryActivityDetailParam param);

    /**
     * 根据活动id获取活动信息
     *
     * @param lotteryActivityId
     * @return
     */
    LotteryActivity getLotteryActivity(Long lotteryActivityId);

    /**
     * 根据主键id批量获取活动信息
     *
     * @param lotteryActivityIdCollection
     * @return
     */
    List<LotteryActivity> getLotteryActivityList(Collection<Long> lotteryActivityIdCollection);

    /**
     * 获取用户未参与的活动记录
     *
     * @param param
     * @return
     */
    Page<LotteryActivity> getNotJoinLotteryActivityList(LoginUserPageVo<LotteryActivity> param);

    /**
     * 中奖用户明细
     *
     * @param param
     * @return
     */
    ApiResult<LotteryWinUserDetailRes> winUserDetail(LotteryWinUserDetailParam param);

    /**
     * 参与用户明细
     *
     * @param param
     * @return
     */
    ApiResult<Page<UserShortInfoRes>> joinUserDetail(LotteryJoinUserDetailParam param);

    /**
     * 计算开奖剩余时间（毫秒）
     *
     * @param now
     * @param openTime
     * @return
     */
    long betweenOpenSurplusTimeMs(Date now, Date openTime);

    /**
     * 查询抽奖活动列表
     * @param param
     * @return
     */
    ApiResult<Page<LotteryActivityListVo>> showList(LotteryActivityListParam param);

    /**
     * 添加抽奖活动
     * @param param
     * @return
     */
    ApiResult<Long> add(LotteryActivityAddParam param);

    /**
     * 上下架抽奖活动
     * @param lotteryActivityId
     * @param productState
     * @return 只有更新成功才会返回值
     */
    ApiResult<LotteryActivity> changeProductState(Long lotteryActivityId, Integer productState);

    /**
     * 查询最新的抽奖活动
     * @param lotteryActivityId
     * @return
     */
    LotteryActivity getLotteryActivityNewEst(Long lotteryActivityId);
}
