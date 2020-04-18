package cn.lcarus.common.enums;

import cn.chinotan.zeus.framework.common.enums.BaseEnum;

import java.util.Objects;

/**
 * 用户三方订阅消息记录状态类型
 *
 * @program: guess
 * @author: xingcheng
 * @create: 2019-11-22 14:49
 **/
public enum UserThirdNoticeStateEnum implements BaseEnum {

    REJECT(0, "表示用户拒绝订阅该条id对应的模板消息"),
    ACCEPT(1, "表示用户同意订阅该条id对应的模板消息"),
    BAN(2, "'表示已被后台封禁"),
    ;

    /**
     * 枚举编码
     */
    private final int code;

    /**
     * 枚举描述
     */
    private final String desc;

    UserThirdNoticeStateEnum(int code, String desc) {
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
    public static UserThirdNoticeStateEnum codeOf(Integer code) {
        UserThirdNoticeStateEnum[] values = values();
        for (UserThirdNoticeStateEnum stateEnum : values) {
            if (Objects.equals(stateEnum.getCode(), code)) {
                return stateEnum;
            }
        }

        return null;
    }

}
