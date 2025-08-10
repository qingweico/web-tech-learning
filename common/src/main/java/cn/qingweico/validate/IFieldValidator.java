package cn.qingweico.validate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author zqw
 * @date 2025/7/26
 */
public interface IFieldValidator<T extends Annotation, TEntity> extends IValidator {

    /**
     * 实体字段校验
     * 当校验注解应用于字段时将调用此接口作校验
     *
     * @param field  待校验字段
     * @param value  待校验字段值
     * @param entity 待校验字段所在数据实体对象(非null)
     * @param anno   应用于字段上的校验注解
     */
    void validate(Field field, Object value, TEntity entity, T anno);

}
