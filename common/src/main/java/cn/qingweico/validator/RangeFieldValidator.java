package cn.qingweico.validator;

import cn.qingweico.convert.Convert;
import cn.qingweico.validate.IFieldValidator;
import cn.qingweico.validate.ValidatorHelper;
import cn.qingweico.validate.anno.Range;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * @author zqw
 * @date 2025/7/26
 */
public class RangeFieldValidator implements IFieldValidator<Range, Object> {
    @Override
    public void validate(Field field, Object value, Object entity, Range anno) {
        if (field.getType().isPrimitive() || field.getType().equals(String.class) || Number.class.isAssignableFrom(field.getType())) {
            BigDecimal v = Convert.toBigDecimal(value);
            if (v.compareTo(BigDecimal.valueOf(anno.max())) > 0 || v.compareTo(BigDecimal.valueOf(anno.min())) < 0) {
                String msg = anno.error();
                String name = ValidatorHelper.getFieldName(field);

                if (StringUtils.isNotEmpty(msg)) {
                    throw new RuntimeException(msg.replace("{0}", name)
                            .replace("{1}", String.valueOf(anno.min()))
                            .replace("{2}", String.valueOf(anno.max())));
                } else {
                    throw new RuntimeException(name + "的格式不符合要求");
                }
            }
        } else {
            throw new RuntimeException("在字段" + ValidatorHelper.getFieldName(field) + "上进行数据验证失败!Range验证只能用在基本类型,字符串及数字的包装类");
        }
    }

}
