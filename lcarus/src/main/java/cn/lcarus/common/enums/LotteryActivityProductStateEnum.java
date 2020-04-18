package cn.lcarus.common.enums;

import cn.chinotan.zeus.framework.common.enums.BaseEnum;

import java.util.Objects;

/**
 * 抽奖活动商品状态
 *
 * @program: guess
 * @author: xingcheng
 * @create: 2019-11-22 14:49
 **/
public enum LotteryActivityProductStateEnum implements BaseEnum {

    DOWN(0, "下架"),
    UP(1, "上架"),
    ;

    /**
     * 枚举编码
     */
    private final int code;

    /**
     * 枚举描述
     */
    private final String desc;

    LotteryActivityProductStateEnum(int code, String desc) {
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
    public static LotteryActivityProductStateEnum codeOf(Integer code) {
        LotteryActivityProductStateEnum[] values = values();
        for (LotteryActivityProductStateEnum stateEnum : values) {
            if (Objects.equals(stateEnum.getCode(), code)) {
                return stateEnum;
            }
        }

        return null;
    }

}
