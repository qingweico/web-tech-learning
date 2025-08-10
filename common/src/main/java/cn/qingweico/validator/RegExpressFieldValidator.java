package cn.qingweico.validator;

import cn.hutool.core.util.ObjectUtil;
import cn.qingweico.validate.IFieldValidator;
import cn.qingweico.validate.ValidatorHelper;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

/**
 * @author zqw
 * @date 2025/7/26
 */
public class RegExpressFieldValidator implements IFieldValidator<Annotation, Object> {

    @Override
    public void validate(Field field, Object fieldValue, Object entity, Annotation anno) {
        if (fieldValue != null) {

            String exp;

            Class<? extends Annotation> annoType = anno.annotationType();

            try {
                exp = annoType.getMethod("regExp").invoke(anno).toString();
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                try {
                    exp = annoType.getMethod("value").invoke(anno).toString();
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("字段校验失败,注解[" + anno.annotationType().getName() + "]不含有regExp()或value()");
                }
            }

            if (StringUtils.isNotEmpty(exp)) {
                if (field.getType().equals(String.class)) {
                    if (ObjectUtil.isNotEmpty(fieldValue)) {
                        exp = exp.replace("/^", "").replace("$/", "");
                        if (!Pattern.matches(exp, (String) fieldValue)) {

                            String msg;
                            try {
                                msg = anno.annotationType().getMethod("error").invoke(anno).toString();
                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                msg = "{0}格式不符合要求";
                            }

                            String name = ValidatorHelper.getFieldName(field);
                            if (StringUtils.isNotEmpty(msg)) {
                                throw new RuntimeException(msg.replace("{0}", name));
                            } else {
                                throw new RuntimeException(name + "的格式不符合要求");
                            }
                        }
                    }
                } else {
                    throw new RuntimeException("在字段" + ValidatorHelper.getFieldName(field) + "上进行数据验证失败!RegExp验证只能用在字符串类型");
                }
            }
        }
    }
}
