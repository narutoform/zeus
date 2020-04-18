package cn.lcarus.admin.controller;

import cn.chinotan.zeus.config.constant.DelayType;
import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.chinotan.zeus.framework.common.service.BaseDelayTaskService;
import cn.chinotan.zeus.framework.common.vo.Task;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.lcarus.admin.param.LotteryActivityAddParam;
import cn.lcarus.admin.param.LotteryActivityListParam;
import cn.lcarus.admin.res.LotteryActivityListVo;
import cn.lcarus.common.entity.LotteryActivity;
import cn.lcarus.common.enums.LotteryActivityProductStateEnum;
import cn.lcarus.common.enums.LotteryActivityTypeEnum;
import cn.lcarus.common.service.BaseLotteryActivityService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @author xincheng
 * @since 2019-10-24
 */
@Slf4j
@RestController
@RequestMapping("/lottery/activity")
@Api("抽奖活动 API")
public class AdminLotteryActivityController extends BaseController {

    @Autowired
    private BaseLotteryActivityService baselotteryActivityService;

    @Autowired
    private BaseDelayTaskService baseDelayTaskService;

    @Value("${lcarus.lottery.join.robot.enable:true}")
    private boolean enableJoinRobot;

    @Value("${lcarus.lottery.join.robot.robot-max-minute}")
    private Integer robotMaxMinute;

    @Value("${lcarus.lottery.join.robot.robot-min-minute}")
    private Integer robotMinMinute;

    @GetMapping("/v1/list")
    @ApiOperation(value = "列表展示", notes = "接口")
    public ApiResult<Page<LotteryActivityListVo>> showList(@Valid LotteryActivityListParam param) {
        return baselotteryActivityService.showList(param);
    }

    @PostMapping("/v1/add")
    @ApiOperation(value = "活动添加", notes = "接口")
    public ApiResult<Long> add(@Valid @RequestBody LotteryActivityAddParam param) {
        if (Objects.equals(param.getType(), LotteryActivityTypeEnum.OBJECT.getCode())) {
            if (Objects.isNull(param.getCostPrice())) {
                return ApiResult.fail(ApiCode.CODE_100004);
            }
        }
        if (DateUtil.compare(param.getOpenTime(), DateUtil.date()) <= 0) {
            return ApiResult.fail(ApiCode.CODE_100005);
        }
        return baselotteryActivityService.add(param);
    }

    @PostMapping("/v1/changeProductState/{lotteryActivityId}/{productState}")
    @ApiOperation(value = "上下架抽奖商品 0下架 1上架", notes = "接口")
    public ApiResult<?> changeProductState(@PathVariable("lotteryActivityId") Long lotteryActivityId, @PathVariable("productState") Integer productState) {
        ApiResult<LotteryActivity> changeProductStateResult = baselotteryActivityService.changeProductState(lotteryActivityId, productState);
        if (!changeProductStateResult.isSuccess()) {
            return changeProductStateResult;
        }
        DateTime now = DateUtil.date();
        if (Objects.equals(productState, LotteryActivityProductStateEnum.UP.getCode()) && Objects.nonNull(changeProductStateResult.getData())) {
            LotteryActivity lotteryActivity = changeProductStateResult.getData();
            // 上架后发送定时开奖
            Task<Long> lotteryOpenTask = new Task<>(DelayType.LOTTERY_OPEN, lotteryActivity.getId(), lotteryActivity.getOpenTime().getTime());
            baseDelayTaskService.sendDelayTask(lotteryOpenTask);
            // 上架后发送定时机器人抽奖
            if (enableJoinRobot) {
                Task<Long> lotteryRobotAutoJoinTask = new Task<>(DelayType.LOTTERY_ROBOT_JOIN, lotteryActivity.getId(), DateUtil.offsetMinute(now, RandomUtil.randomInt(robotMinMinute, robotMaxMinute)).getTime());
                baseDelayTaskService.sendDelayTask(lotteryRobotAutoJoinTask);
            }
        }
        return ApiResult.ok();
    }
}

