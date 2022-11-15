package cn.qingweico.aop.aspect;

import cn.qingweico.aop.service.EchoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 基于 XML 配置自动代理
 *
 * @author zqw
 * @date 2022/7/24
 */
public class XmlAspectAutoProxy {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new
                ClassPathXmlApplicationContext("META-INF/spring-aop-auto-proxy-context.xml");
        EchoService echoService = context.getBean("defaultEchoService", EchoService.class);
        System.out.println(echoService.echo("XmlAspectAutoProxy"));
        context.close();
    }
}
