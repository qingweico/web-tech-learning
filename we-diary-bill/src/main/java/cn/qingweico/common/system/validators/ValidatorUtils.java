package cn.qingweico.common.system.validators;


import cn.qingweico.model.BusinessException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author zhouqingwei
 */
public class ValidatorUtils {

    @SuppressWarnings("resource")
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();


    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的对象
     * @throws cn.qingweico.model.BusinessException 校验不通过, 则抛出 BusinessException 异常
     */
    public static void validateEntity(Object object, Class<?>... groups)
            throws BusinessException {
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint = constraintViolations.iterator().next();
            throw new BusinessException(constraint.getMessage());
        }
    }
}
