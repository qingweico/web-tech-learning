package cn.qingweico.validator;

import cn.hutool.core.util.ObjectUtil;
import cn.qingweico.convert.StringConvert;
import cn.qingweico.validate.IFieldValidator;
import cn.qingweico.validate.ValidatorHelper;
import cn.qingweico.validate.anno.Length;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * @author zqw
 * @date 2025/7/26
 */
public class LengthFieldValidator implements IFieldValidator<Length, Object> {

    @Override
    public void validate(Field field, Object value, Object entity, Length anno) {
        if (!field.getType().equals(String.class)) {
            throw new RuntimeException("在字段" + ValidatorHelper.getFieldName(field) + "上进行数据验证失败!Length验证只能用在String类型");
        }
        if (ObjectUtil.isEmpty(value)) {
            return;
        }
        int strlen = StringConvert.toString(value).length();
        String name = ValidatorHelper.getFieldName(field);
        validateLength(strlen, anno.min(), anno.max(), name, anno.error());
    }

    private void validateLength(int strlen, int min, int max, String name, String msg) {
        if ((min > -1 && strlen < min) || (strlen > max)) {
            throw createValidationException(strlen, min, max, name, msg);
        }
    }


    private RuntimeException createValidationException(int actual, int min, int max, String name, String msg) {
        if (StringUtils.isNotEmpty(msg)) {
            String message = msg
                    .replace("{0}", name)
                    .replace("{1}", min <= 0 ? "0" : String.valueOf(min))
                    .replace("{2}", String.valueOf(max))
                    .replace("{3}", String.valueOf(actual));
            return new RuntimeException(message);
        }
        return new RuntimeException(
                String.format("字段[%s]长度%d不符合要求(最小:%d, 最大:%d)",
                        name, actual, min, max));
    }

}
