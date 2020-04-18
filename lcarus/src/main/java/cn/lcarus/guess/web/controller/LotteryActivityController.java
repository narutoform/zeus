package cn.lcarus.guess.web.controller;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.lcarus.common.entity.LotteryActivity;
import cn.lcarus.common.service.BaseLotteryActivityService;
import cn.lcarus.guess.web.param.LoginUserPageVo;
import cn.lcarus.guess.web.param.LotteryActivityDetailParam;
import cn.lcarus.guess.web.param.LotteryJoinUserDetailParam;
import cn.lcarus.guess.web.param.LotteryWinUserDetailParam;
import cn.lcarus.guess.web.res.LotteryActivityDetailRes;
import cn.lcarus.guess.web.res.LotteryActivityIndexRes;
import cn.lcarus.guess.web.res.LotteryWinUserDetailRes;
import cn.lcarus.guess.web.res.UserShortInfoRes;
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
 * @description: 用户相关接口
 * @author: xingcheng
 * @create: 2020-03-02 21:12
 **/
@Slf4j
@RestController
@RequestMapping("/api/lottery/activity")
@Api(value = "抽奖活动相关接口 API", description = "03.抽奖活动相关接口")
public class LotteryActivityController extends BaseController {

    @Autowired
    private BaseLotteryActivityService baseLotteryActivityService;

    @PostMapping("/v1/index")
    @ApiOperation(value = "首页请求接口")
    public ApiResult<LotteryActivityIndexRes> showInfo(@Valid @RequestBody LoginUserPageVo<LotteryActivity> param) {
        return baseLotteryActivityService.showIndex(param);
    }

    @PostMapping("/v1/detail")
    @ApiOperation(value = "抽奖详情请求接口")
    public ApiResult<LotteryActivityDetailRes> showDetail(@Valid @RequestBody LotteryActivityDetailParam param) {
        return baseLotteryActivityService.showDetail(param);
    }

    @PostMapping("/v1/winUserDetail")
    @ApiOperation(value = "抽奖中奖用户明细查询请求接口")
    public ApiResult<LotteryWinUserDetailRes> winUserDetail(@Valid @RequestBody LotteryWinUserDetailParam param) {
        return baseLotteryActivityService.winUserDetail(param);
    }

    @PostMapping("/v1/joinUserDetail")
    @ApiOperation(value = "抽奖参与用户明细查询请求接口")
    public ApiResult<Page<UserShortInfoRes>> joinUserDetail(@Valid @RequestBody LotteryJoinUserDetailParam param) {
        return baseLotteryActivityService.joinUserDetail(param);
    }
}
