package cn.chinotan.zeus.framework.core.validator.constraints;

import cn.chinotan.zeus.framework.core.validator.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义手机号码正则验证注解
 * @author xingcheng
 * @date 2018-11-08
 */
@Documented
@Constraint(validatedBy = { PhoneValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface Phone {
	String message() default "请输入有效的手机号码";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
