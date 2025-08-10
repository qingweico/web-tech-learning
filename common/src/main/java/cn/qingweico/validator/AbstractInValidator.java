package cn.qingweico.validator;

import cn.hutool.core.util.StrUtil;
import cn.qingweico.validate.IFieldValidator;
import cn.qingweico.validate.ValidatorHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * @author zqw
 * @date 2025/8/10
 */
public abstract class AbstractInValidator<T extends Annotation> implements IFieldValidator<T, Object> {
    protected abstract List<String> getConditionValues(T annotation, Field field);

    protected T annotation;

    @Override
    public void validate(Field field, Object value, Object o, T anno) {

        if (value == null) {
            return;
        }

        List<String> conditions = getConditionValues(anno, field);
        if (conditions.isEmpty()) {
            return;
        }

        if (value instanceof Number) {
            String code = StrUtil.toString(value);
            if (!conditions.contains(code)) {
                throw new RuntimeException(ValidatorHelper.getFieldName(field) + "的取值不在允许的范围内");
            }
        } else if (value instanceof String) {
            if (!conditions.contains(value)) {
                throw new RuntimeException(ValidatorHelper.getFieldName(field) + "的取值不在允许的范围内");
            }
        } else if (value.getClass().isArray()) {
            Object[] arr = (Object[]) value;
            for (Object v : arr) {
                String code = toCode(v);
                if (!conditions.contains(code)) {
                    throw new RuntimeException(ValidatorHelper.getFieldName(field) + "的取值不在允许的范围内");
                }
            }
        } else if (value instanceof Collection) {
            for (Object v : (Collection<?>) value) {
                String code = toCode(v);
                if (!conditions.contains(code)) {
                    throw new RuntimeException(ValidatorHelper.getFieldName(field) + "的取值不在允许的范围内");
                }
            }
        }
    }

    private String toCode(Object value) {
        if (value instanceof Number) {
            return StrUtil.toString(value);
        } else if (value instanceof String) {
            return (String) value;
        } else {
            String msg = String.format("[@%s]只能用于数字和字符串字段", annotation.getClass().getSimpleName());
            throw new RuntimeException(msg);
        }
    }
}
