package cn.lcarus.admin.controller;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.controller.BaseController;
import cn.lcarus.admin.param.EcpmAddParam;
import cn.lcarus.admin.param.EcpmListParam;
import cn.lcarus.admin.res.EcpmListVo;
import cn.lcarus.common.service.BaseEcpmService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author xincheng
 * @since 2019-10-24
 */
@Slf4j
@RestController
@RequestMapping("/ecpm")
@Api("ECPM API")
public class AdminEcpmController extends BaseController {

    @Autowired
    private BaseEcpmService baseEcpmService;

    @GetMapping("/v1/list")
    @ApiOperation(value = "列表展示", notes = "接口")
    public ApiResult<Page<EcpmListVo>> showList(@Valid EcpmListParam param) {
        return baseEcpmService.showList(param);
    }

    @PostMapping("/v1/add")
    @ApiOperation(value = "添加ecpm", notes = "接口")
    public ApiResult<Long> add(@Valid @RequestBody EcpmAddParam param) {
        return baseEcpmService.add(param);
    }

    @PostMapping("/v1/edit/{ecpmId}")
    @ApiOperation(value = "编辑ecpm", notes = "接口")
    public ApiResult<?> edit(@PathVariable("ecpmId") Long ecpmId, @Valid @RequestBody EcpmAddParam param) {
        return baseEcpmService.edit(ecpmId, param);
    }
}

