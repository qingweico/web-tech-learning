package cn.qingweico.utils;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字节码工具
 *
 * @author zqw
 * @date 2023/11/04
 */
public class ClassUtil {

    private ClassUtil() {
    }

    /**
     * 缓存目标类上的泛型值
     */
    private static final Map<Class<?>, Class<?>> CACHE = new ConcurrentHashMap<>(64);

    /**
     * 缓存mongo实体的主键字段
     */
    private static final Map<Class<?>, Field> FIELD_CACHE = new ConcurrentHashMap<>(64);

    /**
     * 获取mongo集合实体的主键值
     *
     * @param obj 目标对象
     * @return id对应的值
     */
    public static Serializable getId(Object obj) {

        Field field = getIdField(obj);
        field.setAccessible(true);
        try {
            return (Serializable) field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("not exist id value");
        }

    }

    /**
     * 获取mongo集合实体的主键字段
     *
     * @param obj 目标对象
     * @return 主建字段
     */
    public static Field getIdField(Object obj) {

        Class<?> clazz = obj.getClass();
        Field result = FIELD_CACHE.get(clazz);
        if (Objects.nonNull(result)) {
            return result;
        }
        for (Field field : clazz.getDeclaredFields()) {
            Id annotation = field.getAnnotation(Id.class);
            if (Objects.nonNull(annotation)) {
                FIELD_CACHE.put(clazz, field);
                return field;
            }
        }
        throw new RuntimeException("no exist id");

    }

    /**
     * 获取对象上的泛型
     *
     * @param obj 目标类
     * @return 类名
     */
    public static Class<?> getGenericsInObject(Object obj) {

        if (Objects.isNull(obj)) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        Class<?> result = CACHE.get(clazz);
        if (Objects.nonNull(result)) {
            return result;
        }
        Type aClass = obj.getClass().getGenericSuperclass();
        Type subType = ((ParameterizedType) aClass).getActualTypeArguments()[1];
        result = (Class<?>) subType;
        CACHE.put(clazz, result);
        return result;

    }

}
