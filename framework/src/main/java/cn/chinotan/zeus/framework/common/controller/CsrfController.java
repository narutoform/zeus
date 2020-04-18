package cn.chinotan.zeus.framework.common.controller;

import cn.chinotan.zeus.framework.log.annotation.OperationLogIgnore;
import cn.chinotan.zeus.framework.util.UUIDUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * CSRF 供swagger调用
 *
 * @author xingcheng
 * @date 2019/12/10
 **/
@ApiIgnore
@OperationLogIgnore
@RestController
public class CsrfController {

    @RequestMapping(value = "/csrf", method = {RequestMethod.GET, RequestMethod.POST})
    public String csrf() {
        return UUIDUtil.getUuid();
    }

}
