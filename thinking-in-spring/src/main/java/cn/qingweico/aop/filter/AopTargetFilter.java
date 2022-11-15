package cn.qingweico.aop.filter;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * AOP 过滤目标方法
 *
 * @author zqw
 * @date 2022/7/20
 */
public class AopTargetFilter {
    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = AopTargetFilter.class.getClassLoader();
        Class<?> targetClass = classLoader.loadClass("cn.qingweico.aop.service.EchoService");

        // Spring 反射工具类
        Method method = ReflectionUtils.findMethod(targetClass, "echo", String.class);
        System.out.println(method);
        ReflectionUtils.doWithMethods(targetClass, m1 -> System.out.println("返回参数类型为Object且只有一个参数的方法: " + m1),
                m2 -> {
            // 获取异常类型
            Class<?>[] exceptionTypes = m2.getExceptionTypes();
            // 获取参数类型
            Class<?>[] parameterTypes = m2.getParameterTypes();
            // 返回参数类型为Object且只有一个参数的方法
            return (parameterTypes.length == 1
                    && Object.class.equals(parameterTypes[0]))
                    // 返回仅抛出 IllegalArgumentException 异常的方法
                    || ( exceptionTypes.length == 1
                    && IllegalArgumentException.class.equals(exceptionTypes[0]));
        });
    }
}
