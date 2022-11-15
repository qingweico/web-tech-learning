package cn.qingweico.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zqw
 * @date 2022/9/24
 */
@ComponentScan(basePackages = "cn.qingweico.annotation")
public class ComponentScanBean {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ComponentScanBean.class);
        String log = context.getBean("log", String.class);
        System.out.println(log);
        context.close();
    }
}
