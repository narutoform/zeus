package cn.lcarus.common.enums;

import cn.chinotan.zeus.framework.common.enums.BaseEnum;

import java.util.Objects;

/**
 * 用户分享记录状态类型
 *
 * @program: guess
 * @author: xingcheng
 * @create: 2019-11-22 14:49
 **/
public enum UserShareRecordStateEnum implements BaseEnum {

    GET_ABLE(0, "可领取"),
    GET(1, "已领取"),
    ;

    /**
     * 枚举编码
     */
    private final int code;

    /**
     * 枚举描述
     */
    private final String desc;

    UserShareRecordStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    /**
     * 根据枚举code值转化为枚举对象
     *
     * @param code 枚举code值
     * @return 枚举对象
     */
    public static UserShareRecordStateEnum codeOf(Integer code) {
        UserShareRecordStateEnum[] values = values();
        for (UserShareRecordStateEnum stateEnum : values) {
            if (Objects.equals(stateEnum.getCode(), code)) {
                return stateEnum;
            }
        }

        return null;
    }

}
