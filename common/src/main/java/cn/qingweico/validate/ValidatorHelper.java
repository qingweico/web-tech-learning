package cn.qingweico.validate;

import cn.hutool.core.util.ReflectUtil;
import cn.qingweico.model.ParamValidateException;
import cn.qingweico.validate.anno.Name;
import cn.qingweico.validate.anno.ValidateAnnotation;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author zqw
 * @date 2025/7/26
 */

@Setter
public class ValidatorHelper {

    @Autowired
    private ApplicationContext applicationContext;

    @Getter
    public static class ValidateHolder {
        /**
         * 错误信息 字段名为key 错误信息为value
         */
        private final Map<String, String> error = new HashMap<>();

        /**
         * 已验证过的对象
         */
        public List<Object> validated = new ArrayList<>();

        public void addError(String field, String msg) {
            String old = error.get(field);
            if (StringUtils.isEmpty(old)) {
                old = msg;
            } else {
                old = old + "," + msg;
            }
            error.put(field, old);
        }

        public boolean hasError() {
            return !error.isEmpty();
        }
    }


    private static boolean hasChild(Class<?> clazz) {
        return !clazz.isPrimitive() && !String.class.isAssignableFrom(clazz) && !Boolean.class.isAssignableFrom(clazz) && !Character.class.isAssignableFrom(clazz) && !Byte.class.isAssignableFrom(clazz) && !Short.class.isAssignableFrom(clazz) && !Integer.class.isAssignableFrom(clazz) && !Long.class.isAssignableFrom(clazz) && !Float.class.isAssignableFrom(clazz) && !Double.class.isAssignableFrom(clazz) && !Void.class.isAssignableFrom(clazz) && !Map.class.isAssignableFrom(clazz) && !Collection.class.isAssignableFrom(clazz) && !Date.class.isAssignableFrom(clazz) && !BigDecimal.class.isAssignableFrom(clazz) && !clazz.isEnum() && !clazz.isInterface() && !clazz.isArray();
    }

    /**
     * 应用验证器对对象进行字段验证 若验证失败将抛出异常
     *
     * @param obj 需要验证字段的对象
     */
    public void validateForException(Object obj) {
        validateForException(obj, null);
    }

    public void validateForException(Object obj, String[] ignore) {
        ValidateHolder holder = new ValidateHolder();
        validate(obj, holder, ignore);
        if (holder.hasError()) {
            throw new ParamValidateException(holder.getError());
        }
    }

    /**
     * 应用验证器对对象进行字段验证
     *
     * @param obj 需要验证字段的对象
     * @return 验证是否通过
     */
    public boolean validate(Object obj) {
        ValidateHolder holder = new ValidateHolder();
        validate(obj, holder, null);
        return !holder.hasError();
    }

    /**
     * 应用验证器对对象进行字段验证 若验证失败则在holder中返回验证结果
     *
     * @param obj     对象
     * @param holder  调用方需传入的holder,若验证失败,holder中将会包含错误信息
     * @param ignores 忽略的字段
     */
    public void validate(Object obj, ValidateHolder holder, String[] ignores) {
        if (obj == null) {
            throw new RuntimeException("验证失败,不能对空对象进行验证");
        }
        if (!holder.validated.contains(obj)) {
            holder.validated.add(obj);

            // 验证校验
            for (Annotation a : obj.getClass().getAnnotations()) {
                ValidateAnnotation valAnno = a.annotationType().getAnnotation(ValidateAnnotation.class);
                if (valAnno != null) {
                    Set<IEntityValidator> validators = getModuleValidators(valAnno);
                    for (IEntityValidator validator : validators) {
                        try {
                            validator.validate(obj, a);
                        } catch (Exception e) {
                            holder.addError("object", e.getMessage());
                        }
                    }
                }
            }

            // 验证字段校验
            if (hasChild(obj.getClass())) {
                Field[] allFields = ReflectUtil.getFields(obj.getClass());

                for (Field f : allFields) {
                    if (ignores == null || !Arrays.asList(ignores).contains(f.getName())) {
                        // 校验字段
                        ReflectionUtils.makeAccessible(f);
                        List<Annotation> annotations = new ArrayList<>();
                        Annotation[] ans = f.getAnnotations();
                        for (Annotation a : ans) {
                            if (a.annotationType().getAnnotation(ValidateAnnotation.class) != null) {
                                annotations.add(a);
                            }
                        }
                        for (Annotation anno : annotations) {
                            try {
                                validate(obj, f, f.get(obj), anno, holder);
                            } catch (Exception e) {
                                throw new RuntimeException("在字段" + getFieldName(f) + "上进行数据验证失败!", e);
                            }
                        }

                        // 若字段有子属性,继续校验
                        try {
                            Object v;
                            if (hasChild(f.getType()) && (v = f.get(obj)) != null) {
                                validate(v, holder, null);
                            }
                        } catch (IllegalAccessException ignored) {

                        }
                    }
                }
            }
        }
    }

    private void validate(Object obj, Field field, Object value, Annotation annotation, ValidateHolder holder) {

        ValidateAnnotation validAnno = annotation.annotationType().getAnnotation(ValidateAnnotation.class);
        if ((value == null || "".equals(value)) && !validAnno.nullable()) {
            return;
        }

        Set<IFieldValidator> validators = getFieldValidators(validAnno);

        if (!validators.isEmpty()) {
            for (IFieldValidator validator : validators) {
                try {
                    validator.validate(field, value, obj, annotation);
                } catch (RuntimeException e) {
                    holder.addError(getFieldName(field), e.getMessage());
                }
            }
        }
    }

    private ThreadLocal<Map<ValidateAnnotation, Set<IFieldValidator>>> fieldValidatorMap = new ThreadLocal<>();
    private ThreadLocal<Map<ValidateAnnotation, Set<IEntityValidator>>> moduleValidatorMap = new ThreadLocal<>();

    private Set<IFieldValidator> getFieldValidators(ValidateAnnotation annotation) {
        Map<ValidateAnnotation, Set<IFieldValidator>> map = fieldValidatorMap.get();
        if (map == null) {
            map = new HashMap<>();
            fieldValidatorMap.set(map);
        }
        Set<IFieldValidator> set = map.get(annotation);
        if (set == null) {
            set = new HashSet<>();

            Class<? extends IValidator>[] clz = annotation.value();
            if (clz != null) {
                for (Class<? extends IValidator> c : clz) {
                    try {
                        IValidator bean = applicationContext.getBean(c);
                        if (bean instanceof IFieldValidator) {
                            set.add((IFieldValidator) bean);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("验证类[" + c + "]不存在");
                    }
                }
            }
            String[] clzNames = annotation.className();
            if (clzNames != null && clzNames.length > 0) {
                for (String n : annotation.className()) {
                    try {
                        Class<?> c = Class.forName(n);
                        Object bean = applicationContext.getBean(c);
                        if (bean instanceof IFieldValidator) {
                            set.add((IFieldValidator) bean);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("验证类[" + n + "]不存在");
                    }
                }
            }

            map.put(annotation, set);
        }

        return set;
    }

    private Set<IEntityValidator> getModuleValidators(ValidateAnnotation annotation) {
        Map<ValidateAnnotation, Set<IEntityValidator>> map = moduleValidatorMap.get();
        if (map == null) {
            map = new HashMap<>();
            moduleValidatorMap.set(map);
        }
        Set<IEntityValidator> set = map.get(annotation);
        if (set == null) {
            set = new HashSet<>();

            Class<? extends IValidator>[] clz = annotation.value();
            if (clz != null) {
                for (Class<? extends IValidator> c : clz) {
                    try {
                        IValidator bean = applicationContext.getBean(c);
                        if (bean instanceof IEntityValidator) {
                            set.add((IEntityValidator) bean);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("验证类[" + c + "]不存在");
                    }
                }
            }
            String[] clzNames = annotation.className();
            if (clzNames != null && clzNames.length > 0) {
                for (String n : annotation.className()) {
                    try {
                        Class<?> c = Class.forName(n);
                        Object bean = applicationContext.getBean(c);
                        if (bean instanceof IEntityValidator) {
                            set.add((IEntityValidator) bean);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("验证类[" + n + "]不存在");
                    }
                }
            }

            map.put(annotation, set);
        }

        return set;
    }


    public static String getFieldName(Field field) {
        Name n = field.getAnnotation(Name.class);
        if (n != null) {
            return "[" + n.value() + "(" + field.getName() + ")]";
        } else {
            return "[" + field.getName() + "]";
        }
    }
}
