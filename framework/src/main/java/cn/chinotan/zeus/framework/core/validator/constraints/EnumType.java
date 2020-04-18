package cn.chinotan.zeus.framework.core.validator.constraints;

import cn.chinotan.zeus.framework.common.enums.BaseEnum;
import cn.chinotan.zeus.framework.core.validator.EnumTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 枚举类型注解
 * @author xingcheng
 * @date 2018-11-08
 */
@Documented
@Constraint(validatedBy = { EnumTypeValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface EnumType {
	String message() default "请输入正确的类型值";

	Class<? extends BaseEnum> type();

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
