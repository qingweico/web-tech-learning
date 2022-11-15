package cn.qingweico.aop.pointcut;

import cn.qingweico.aop.aspect.AspectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 基于注解的切入点配置
 *
 * @author zqw
 * @date 2022/7/23
 */
@EnableAspectJAutoProxy
public class AnnotationAspectjPointcut {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AnnotationAspectjPointcut.class, AspectConfig.class);
        context.refresh();
        AnnotationAspectjPointcut aspectjPointcut = context.getBean(AnnotationAspectjPointcut.class);
        aspectjPointcut.execute();
        context.close();
    }

    public void execute() {
        System.out.println("exec AspectJPointcut#execute");
    }
}
