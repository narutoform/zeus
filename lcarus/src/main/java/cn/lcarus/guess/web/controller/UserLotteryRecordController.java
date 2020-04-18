package cn.lcarus.guess.web.controller;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.lcarus.common.service.BaseUserLotteryRecordService;
import cn.lcarus.guess.web.param.LotteryActivityJoinParam;
import cn.lcarus.guess.web.param.UserAfterOpenDrawParam;
import cn.lcarus.guess.web.param.UserLotteryRecordParam;
import cn.lcarus.guess.web.res.LotteryActivityJoinRes;
import cn.lcarus.guess.web.res.UserLotteryRecordRes;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: guess
 * @author: xingcheng
 * @create: 2020-03-02 21:12
 **/
@Slf4j
@RestController
@RequestMapping("/api/user/lottery")
@Api(value = "用户抽奖记录接口 API", description = "06.用户抽奖记录接口")
public class UserLotteryRecordController extends BaseController {

    @Autowired
    private BaseUserLotteryRecordService baseUserLotteryRecordService;

    @PostMapping("/v1/record/list")
    @ApiOperation(value = "用户抽奖和中奖记录信息展示接口")
    public ApiResult<Page<UserLotteryRecordRes>> showRecordList(@Valid @RequestBody UserLotteryRecordParam param) {
        return baseUserLotteryRecordService.showRecordList(param);
    }

    @PostMapping("/v1/join")
    @ApiOperation(value = "参与抽奖请求接口")
    public ApiResult<LotteryActivityJoinRes> join(@Valid @RequestBody LotteryActivityJoinParam param) {
        return baseUserLotteryRecordService.join(param);
    }

    @PostMapping("/v1/afterOpen/draw")
    @ApiOperation(value = "开奖后用户确认领取接口")
    public ApiResult<?> afterOpenDraw(@Valid @RequestBody UserAfterOpenDrawParam param) {
        return baseUserLotteryRecordService.draw(param);
    }
}
