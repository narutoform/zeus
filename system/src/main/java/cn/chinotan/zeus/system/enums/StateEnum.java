package cn.chinotan.zeus.system.enums;

import cn.chinotan.zeus.framework.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 启用禁用状态枚举
 *
 * @author xingcheng
 * @date 2019-10-24
 **/
@Getter
@AllArgsConstructor
public enum StateEnum implements BaseEnum {

    /** 禁用 **/
    DISABLE(0, "禁用"),
    /** 启用 **/
    ENABLE(1, "启用");

    private Integer code;
    private String desc;

}
