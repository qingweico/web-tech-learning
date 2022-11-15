package cn.qingweico.aop.aspect;

import cn.qingweico.aop.service.EchoService;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zqw
 * @date 2022/7/23
 */
@Aspect
public class SimpleEnableAop {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring-aop-context.xml");
        // 获取代理对象(echoServiceProxyFactoryBean) 而不是被代理对象(echoService)
        EchoService echoService = context.getBean("echoServiceProxyFactoryBean", EchoService.class);
        System.out.println(echoService.echo("SimpleEnableAop"));
        context.close();

    }
}
