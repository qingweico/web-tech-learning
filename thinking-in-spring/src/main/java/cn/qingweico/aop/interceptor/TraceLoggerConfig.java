package cn.qingweico.aop.interceptor;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.*;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Spring 中已经定义好的 Interceptor
 *
 * @author zqw
 * @date 2023/9/20
 * {@link CustomizableTraceInterceptor} 可自定义跟踪输出
 * {@link SimpleTraceInterceptor} 获取基本信息
 * {@link DebugInterceptor} 获取完整信息
 * {@link PerformanceMonitorInterceptor} 用于测量性能, 可以精确到毫秒级, 适用于数据
 * {@link AsyncExecutionInterceptor} 允许异步方法调用
 * {@link ConcurrencyThrottleInterceptor} 可以限制对象中的线程数, 实际可以设置并执行的线程数
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TraceLoggerConfig {

    @Bean
    public CustomizableTraceInterceptor customizableTraceInterceptor() {
        CustomizableTraceInterceptor customizableTraceInterceptor = new CustomizableTraceInterceptor();
        customizableTraceInterceptor.setUseDynamicLogger(true);
        customizableTraceInterceptor.setEnterMessage("Entering $[methodName]($[arguments])");
        customizableTraceInterceptor.setExitMessage("Leaving  $[methodName](), returned $[returnValue]");
        return customizableTraceInterceptor;
    }

    @Bean
    public Advisor jpaRepositoryAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public * org.springframework.data.jpa.repository.JpaRepository+.*(..))");
        return new DefaultPointcutAdvisor(pointcut, customizableTraceInterceptor());
    }
}
