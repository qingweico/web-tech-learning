package cn.qingweico.validate;

import java.lang.annotation.Annotation;

/**
 * @author zqw
 * @date 2025/7/26
 */
public interface IEntityValidator<T extends Annotation, TEntity> extends IValidator {

    /**
     * 当校验注解应用于实体类时将调用此接口作校验
     *
     * @param entity TEntity
     * @param anno   应用于实体上的校验注解
     */
    void validate(TEntity entity, T anno);

}
