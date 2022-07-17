package cn.qingweico.generic;

import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

/**
 * @author zqw
 * @date 2022/1/1
 */
public class SpringGenericTypeResolver {
    public static void main(String[] args) throws NoSuchMethodException {
        Class<?> resolverClass = SpringGenericTypeResolver.class;
        printReturnTypeGenericInfo(resolverClass, Comparable.class, "empty");
        printReturnTypeGenericInfo(resolverClass, List.class, "stringList");
        printReturnTypeGenericInfo(resolverClass, ArrayList.class, "getStringList");

        // TypeVariable
        @SuppressWarnings("rawtypes")
        Map<TypeVariable, Type> typeVariableMap = GenericTypeResolver.getTypeVariableMap(StringList.class);
        System.out.println(typeVariableMap);

    }

    @SuppressWarnings("unused")
    private static String empty() {
        return "";
    }

    @SuppressWarnings("unused")
    private static <E> List<E> stringList() {
        return null;
    }

    @SuppressWarnings("unused")
    private static StringList getStringList() {
        return new StringList();
    }

    private static void printReturnTypeGenericInfo(Class<?> clazz, Class<?> genericIfc, String methodName, Class<?>... args) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod(methodName, args);

        Class<?> returnType = GenericTypeResolver.resolveReturnType(method, clazz);
        Class<?> returnGenericType = GenericTypeResolver.resolveReturnTypeArgument(method, genericIfc);
        System.out.printf("[ %s ] returnType: %s%n", methodName, returnType);
        System.out.printf("[ %s ] returnGenericType: %s%n", methodName, returnGenericType);
    }
}
