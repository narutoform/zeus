package cn.lcarus.common.enums;

import cn.chinotan.zeus.framework.common.enums.BaseEnum;

import java.util.Objects;

/**
 * 用户账户记录状态类型
 *
 * @program: guess
 * @author: xingcheng
 * @create: 2019-11-22 14:49
 **/
public enum UserAccountRecordStateEnum implements BaseEnum {

    INIT(0, "初始化"),
    ING(10, "进行中（需等待）"),
    ING_FAIL(20, "进行中（可重试）"),
    SUCCESS(30, "成功"),
    FINAL_FAIL(40, "最终失败"),
    ;

    /**
     * 枚举编码
     */
    private final int code;

    /**
     * 枚举描述
     */
    private final String desc;

    UserAccountRecordStateEnum(int code, String desc) {
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
    public static UserAccountRecordStateEnum codeOf(Integer code) {
        UserAccountRecordStateEnum[] values = values();
        for (UserAccountRecordStateEnum stateEnum : values) {
            if (Objects.equals(stateEnum.getCode(), code)) {
                return stateEnum;
            }
        }

        return null;
    }

}
