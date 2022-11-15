package cn.qingweico.aop.pointcut;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 基于XML的切入点配置
 *
 * @author zqw
 * @date 2022/7/23
 */
@EnableAspectJAutoProxy
public class XmlAspectjPointcut {
    public void before() {
        System.out.printf("[%s] before any public method%n", this.getClass().getSimpleName());
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new
                ClassPathXmlApplicationContext("META-INF/spring-aop-context.xml");
        XmlAspectjPointcut xmlAspectjPointcut = context.getBean(XmlAspectjPointcut.class);
        xmlAspectjPointcut.print();
        context.close();
    }

    public void print() {
        System.out.println("public print");
    }

}
