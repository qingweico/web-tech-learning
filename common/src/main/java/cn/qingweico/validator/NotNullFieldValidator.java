package cn.qingweico.validator;

import cn.hutool.core.util.ObjectUtil;
import cn.qingweico.validate.IFieldValidator;
import cn.qingweico.validate.ValidatorHelper;
import cn.qingweico.validate.anno.Required;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author zqw
 * @date 2025/7/26
 */
public class NotNullFieldValidator implements IFieldValidator<Required, Object> {

    @Override
    public void validate(Field field, Object fieldValue, Object entity, Required anno) {
        if (fieldValue == null || isEmptyAny(fieldValue)) {

            String msg = anno.error();

            String name = ValidatorHelper.getFieldName(field);

            if (field.getType().isPrimitive()) {
                throw new RuntimeException("在字段" + ValidatorHelper.getFieldName(field) + "上进行数据验证失败!Required验证不可用于基本数据类型");
            }

            if (StringUtils.isNotEmpty(msg)) {
                throw new RuntimeException(msg.replace("{0}", name));
            } else {
                throw new RuntimeException(name + "不可为空");
            }
        }
    }

    private boolean isEmptyAny(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).stream().anyMatch(ObjectUtil::isEmpty);
        }
        if (obj.getClass().isArray()) {
            return Arrays.stream(((Object[]) obj)).anyMatch(ObjectUtil::isEmpty);
        }
        return ObjectUtil.isEmpty(obj);
    }
}
