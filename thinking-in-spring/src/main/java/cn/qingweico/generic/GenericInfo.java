package cn.qingweico.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * @author zqw
 * @date 2022/1/1
 */
public class GenericInfo {
    public static void main(String[] args) {
        // 泛型参数类型(带参数的泛型类型)
        ParameterizedType parameterizedType = (ParameterizedType) ArrayList.class.getGenericSuperclass();
        // java.util.AbstractList<E>
        System.out.println(parameterizedType);

        // 不带 泛型参数<E> -> java.util.AbstractList
        System.out.println(parameterizedType.getRawType());
        System.out.println(parameterizedType.getOwnerType());
        // 泛型类型变量
        Type[] typeVariable = parameterizedType.getActualTypeArguments();
        Stream.of(typeVariable).map(TypeVariable.class::cast).forEach(System.out::println);
    }
}
