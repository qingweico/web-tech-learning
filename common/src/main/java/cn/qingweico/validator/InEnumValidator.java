package cn.qingweico.validator;

import cn.qingweico.validate.anno.InEnum;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zqw
 * @date 2025/7/26
 */
public class InEnumValidator extends AbstractInValidator<InEnum> {

    @Override
    @SuppressWarnings("unchecked")
    protected List<String> getConditionValues(InEnum annotation, Field field) {
        Class<Enum<?>> enumClass = (Class<Enum<?>>) annotation.value();
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
