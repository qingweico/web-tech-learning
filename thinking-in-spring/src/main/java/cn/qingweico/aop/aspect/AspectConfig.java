package cn.qingweico.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 基于注解的切面配置
 *
 * @author zqw
 * @date 2022/7/23
 */
@Aspect
public class AspectConfig {
    // pointcut 仅起到过滤的作用
    @Pointcut("execution(public * *(..))")
    private void anyPublicMethod() {
        // Pointcut methods should have empty body
    }

    // @Around 与 @Before的区别在于前者需要主要调用目标方法

    @Around("anyPublicMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.printf("[%s] around any public method%n", this.getClass().getSimpleName());
        return joinPoint.proceed();
    }


    @Before("anyPublicMethod()")
    public void before() {
        System.out.printf("[%s] before any public method%n", this.getClass().getSimpleName());
    }


}
