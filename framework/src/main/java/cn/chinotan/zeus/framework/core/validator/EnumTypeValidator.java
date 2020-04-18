package cn.chinotan.zeus.framework.core.validator;

import cn.chinotan.zeus.framework.common.exception.BusinessException;
import cn.chinotan.zeus.framework.core.validator.constraints.EnumType;
import cn.chinotan.zeus.framework.common.enums.BaseEnum;
import cn.chinotan.zeus.framework.util.BaseEnumUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义系统内的枚举验证注解实现类
 *
 * @author xingcheng
 * @date 2018-11-08
 */
public class EnumTypeValidator implements ConstraintValidator<EnumType, Integer> {

    private Class<? extends BaseEnum> baseEnum;

    @Override
    public void initialize(EnumType parameters) {
        baseEnum = parameters.type();
        if (baseEnum == null) {
            throw new BusinessException("请传入枚举类型类");
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        return BaseEnumUtil.exists(baseEnum, value);
    }
}
