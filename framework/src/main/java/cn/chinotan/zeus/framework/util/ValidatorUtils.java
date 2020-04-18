package cn.chinotan.zeus.framework.util;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * @description: 验证参数
 * @author: xingcheng
 * @create: 2020-03-02 17:08
 **/
public class ValidatorUtils {

    public static Optional<String> validateModel(Object obj) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = null;
        constraintViolations = validator.validate(obj);
        Iterator<ConstraintViolation<Object>> iterator = constraintViolations.iterator();
        while (iterator.hasNext()) {
            String message = iterator.next().getMessage();
            if (StringUtils.isNotBlank(message)) {
                return Optional.of(message);
            }
        }
        return Optional.empty();
    }

}
