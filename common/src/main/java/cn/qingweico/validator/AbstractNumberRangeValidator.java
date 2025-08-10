package cn.qingweico.validator;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.qingweico.convert.Convert;
import cn.qingweico.validate.IFieldValidator;
import cn.qingweico.validate.ValidatorHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * @author zqw
 * @date 2025/8/10
 */
public abstract class AbstractNumberRangeValidator<T extends Annotation> implements IFieldValidator<T, Object> {
    protected abstract int compare(BigDecimal fieldValue, BigDecimal limitValue);

    protected abstract String getDefaultErrorMessage();

    protected T annotation;

    @Override
    public void validate(Field field, Object value, Object o, T anno) {
        if (value == null) {
            return;
        }
        if (!isSupportedType(field.getType())) {
            String errorMsg = String.format("在字段%s上进行数据验证失败![@%s]验证只能用在基本类型,字符串及数字的包装类",
                    ValidatorHelper.getFieldName(field), annotation.getClass().getSimpleName());
            throw new RuntimeException(errorMsg);
        }
        BigDecimal v = Convert.toBigDecimal(value);
        BigDecimal limitValue = Convert.toBigDecimal(AnnotationUtils.getValue(anno, "value"));
        String error = AnnotationUtil.getAnnotationValue(field, anno.annotationType(), "error");
        if (compare(v, limitValue) > 0) {
            throw createValidationException(field, limitValue, error);
        }
    }

    private RuntimeException createValidationException(Field field, BigDecimal limitValue, String msg) {
        String name = ValidatorHelper.getFieldName(field);

        if (StringUtils.isNotEmpty(msg)) {
            String message = msg.replace("{0}", name).replace("{1}", String.valueOf(limitValue));
            return new RuntimeException(message);
        }

        return new RuntimeException(String.format(getDefaultErrorMessage(), name, limitValue));
    }


    private boolean isSupportedType(Class<?> fieldType) {
        return fieldType.isPrimitive() || fieldType.equals(String.class) || Number.class.isAssignableFrom(fieldType);
    }

}
