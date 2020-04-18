package cn.lcarus.common.enums;

import cn.chinotan.zeus.framework.common.enums.BaseEnum;

import java.util.Objects;

/**
 * 用户抽奖活动记录状态
 *
 * @program: guess
 * @author: xingcheng
 * @create: 2019-11-22 14:49
 **/
public enum UserLotteryRecordStateEnum implements BaseEnum {

    JOIN(0, "参与抽奖(初始化)"),
    WIN(1, "已中奖"),
    NO_WIN(2, "未中奖"),
    ;

    /**
     * 枚举编码
     */
    private final int code;

    /**
     * 枚举描述
     */
    private final String desc;

    UserLotteryRecordStateEnum(int code, String desc) {
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
    public static UserLotteryRecordStateEnum codeOf(Integer code) {
        UserLotteryRecordStateEnum[] values = values();
        for (UserLotteryRecordStateEnum stateEnum : values) {
            if (Objects.equals(stateEnum.getCode(), code)) {
                return stateEnum;
            }
        }

        return null;
    }

}
