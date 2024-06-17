package cn.qingweico.aop.event;

import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.EventPublicationInterceptor;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;

/**
 * Spring AOP 在 Spring 事件中的使用
 *
 * @author zqw
 * @date 2022/9/22
 */
@EnableAspectJAutoProxy
public class SpringEventPublicationInterceptor {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SpringEventPublicationInterceptor.class, AopExecutor.class, StaticExecutor.class);
        context.refresh();
        // 执行目标方法
        AopExecutor aopExecutor = context.getBean(AopExecutor.class);
        aopExecutor.execute();
        StaticExecutor staticExecutor = context.getBean(StaticExecutor.class);
        staticExecutor.execute();

        context.close();
    }

    @Bean
    public EventPublicationInterceptor eventPublicationInterceptor() {
        EventPublicationInterceptor eventPublicationInterceptor = new EventPublicationInterceptor();
        // 关联自定义事件类型
        eventPublicationInterceptor.setApplicationEventClass(ExecutedEvent.class);
        return eventPublicationInterceptor;
    }

    // 实现 Pointcut

    @Bean
    public Pointcut pointcut() {
        return new StaticMethodMatcherPointcut() {

            @Override
            public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass) {
                return "execute".equals(method.getName()) && AopExecutor.class.equals(targetClass);
            }
        };
    }

    // 声明一个 Advisor Bean

    @Bean
    public PointcutAdvisor pointcutAdvisor(Pointcut pointcut, EventPublicationInterceptor eventPublicationInterceptor) {
        // !!!EventPublicationInterceptor is MethodInterceptor is Advice
        return new DefaultPointcutAdvisor(pointcut, eventPublicationInterceptor);
    }

    // 处理事件

    @EventListener(ExecutedEvent.class)
    public void onListener(ExecutedEvent event) {
        System.out.println(event);
    }
}
