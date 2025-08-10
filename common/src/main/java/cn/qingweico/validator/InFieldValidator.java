package cn.qingweico.validator;

import cn.qingweico.validate.anno.In;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author zqw
 * @date 2025/7/26
 */
public class InFieldValidator extends AbstractInValidator<In> {
    @Override
    protected List<String> getConditionValues(In annotation, Field field) {
        return Arrays.asList(annotation.value());
    }
}
