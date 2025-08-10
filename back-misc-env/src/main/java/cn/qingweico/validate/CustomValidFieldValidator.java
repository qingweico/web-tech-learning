package cn.qingweico.validate;

import cn.qingweico.validate.anno.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Component
public class CustomValidFieldValidator implements IFieldValidator<Valid, Object>, IEntityValidator<Valid, Object> {

    @Autowired
    ApplicationContext context;

    @Override
    public void validate(Field field, Object value, Object entity, Valid anno) {
        if (anno.value() != null) {
            IValidator validator;
            try {
                validator = context.getBean(anno.value());
            } catch (Exception e) {
                throw new RuntimeException("验证类[" + anno.value() + "]不存在");
            }
            if (validator instanceof IFieldValidator) {
                ((IFieldValidator) validator).validate(field, value, entity, anno);
            }
        }
    }

    @Override
    public void validate(Object entity, Valid anno) {
        if (anno.value() != null) {
            IValidator validator;
            try {
                validator = context.getBean(anno.value());
            } catch (Exception e) {
                throw new RuntimeException("验证类[" + anno.value() + "]不存在");
            }
            if (validator instanceof IEntityValidator) {
                ((IEntityValidator) validator).validate(entity, anno);
            }
        }
    }
}
