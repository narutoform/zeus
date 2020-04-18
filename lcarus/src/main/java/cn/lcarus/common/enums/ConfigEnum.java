package cn.lcarus.common.enums;

import java.util.Objects;

/**
 * 配置
 * @program: guess
 * @author: xingcheng
 * @create: 2019-11-22 14:49
 **/
public enum ConfigEnum {

    WITH_DRAW_LIMIT_AMOUNT_DOWN("with_draw_limit_amount_down", "100", "每次提现的最低限 分"),
    WITH_DRAW_LIMIT_AMOUNT_UP("with_draw_limit_amount_up", "20000", "每次提现的最高限 分"),
    WITH_DRAW_ENABLE_SHOW("with_draw_enable_show", "true", "是否展示提现按钮"),
    LOTTERY_SHARE_LIMIT_COUNT_DAY_UP("lottery_share_limit_count_day_up", "10", "每日分享限制"),
    USER_ACCOUNT_DRAW_APPROVE_ENABLE("user_account_draw_approve_enable", "false", "是否开启用户账户提现审核功能 true:开启 false:关闭"),
    ;

    /**
     * 枚举编码
     */
    public final String key;

    /**
     * 枚举描述
     */
    public final String value;

    /**
     * 枚举描述
     */
    public final String desc;

    ConfigEnum(String key, String value, String desc) {
        this.key = key;
        this.value = value;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据枚举key值转化为枚举对象
     *
     * @param key 枚举值
     * @return 枚举对象
     */
    public static ConfigEnum keyOf(String key) {
        ConfigEnum[] values = values();
        for (ConfigEnum stateEnum : values) {
            if (Objects.equals(stateEnum.getKey(), key)) {
                return stateEnum;
            }
        }

        return null;
    }

}
