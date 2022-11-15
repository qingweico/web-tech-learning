package cn.qingweico.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zqw
 * @date 2022/9/24
 */
public class ConfigurationBean {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(LogPrintConfig.class);
        context.refresh();
        String log = context.getBean("log", String.class);
        System.out.println(log);
        context.close();
    }
}
