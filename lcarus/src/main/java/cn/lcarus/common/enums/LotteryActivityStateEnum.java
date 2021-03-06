package cn.lcarus.common.enums;

import cn.chinotan.zeus.framework.common.enums.BaseEnum;

import java.util.Objects;

/**
 * 抽奖活动状态
 *
 * @program: guess
 * @author: xingcheng
 * @create: 2019-11-22 14:49
 **/
public enum LotteryActivityStateEnum implements BaseEnum {

    WAIT_ENABLE(0, "待开启"),
    WAIT_OPEN(1, "待开奖"),
    END(2, "已结束"),
    ;

    /**
     * 枚举编码
     */
    private final int code;

    /**
     * 枚举描述
     */
    private final String desc;

    LotteryActivityStateEnum(int code, String desc) {
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
    public static LotteryActivityStateEnum codeOf(Integer code) {
        LotteryActivityStateEnum[] values = values();
        for (LotteryActivityStateEnum stateEnum : values) {
            if (Objects.equals(stateEnum.getCode(), code)) {
                return stateEnum;
            }
        }

        return null;
    }

}
