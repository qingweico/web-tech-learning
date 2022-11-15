package cn.qingweico.aop.pointcut;

import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author zqw
 * @date 2022/7/23
 */
public class EchoServicePointcut extends StaticMethodMatcherPointcut {

    private final String methodName;
    private final Class<?> targetClass;

    public EchoServicePointcut(String methodName, Class<?> targetClass) {
        this.methodName = methodName;
        this.targetClass = targetClass;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        // targetClass 是 this.targetClass 的子类或者相同类型
        return Objects.equals(method.getName(), methodName) && this.targetClass.isAssignableFrom(targetClass);
    }
}
