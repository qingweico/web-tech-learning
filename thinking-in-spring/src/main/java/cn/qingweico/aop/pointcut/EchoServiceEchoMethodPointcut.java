package cn.qingweico.aop.pointcut;

import cn.qingweico.aop.service.EchoService;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author zqw
 * @date 2022/7/23
 */
public class EchoServiceEchoMethodPointcut implements Pointcut {
    public static final EchoServiceEchoMethodPointcut INSTANCE = new EchoServiceEchoMethodPointcut();
    private EchoServiceEchoMethodPointcut(){}

    @Override
    public ClassFilter getClassFilter() {
        // 匹配 EchoService 类型、EchoService子接口 或者子类型
        return EchoService.class::isAssignableFrom;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return new MethodMatcher() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                return "echo".equals(method.getName())
                        && method.getParameters().length == 1
                        && Objects.equals(String.class, method.getParameterTypes()[0]);
            }

            @Override
            public boolean isRuntime() {
                return false;
            }

            @Override
            public boolean matches(Method method, Class<?> targetClass, Object... args) {
                return false;
            }
        };
    }
}
