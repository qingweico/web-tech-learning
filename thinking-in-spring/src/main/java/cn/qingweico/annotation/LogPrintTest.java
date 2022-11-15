package cn.qingweico.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author zqw
 * @date 2022/9/24
 */
public class LogPrintTest {
    @Bean
    public LogPrintFactoryBean logPrintFactoryBean() {
        return new LogPrintFactoryBean();
    }

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(LogPrintTest.class, LogPrintConfig.class);
        context.refresh();
        LogPrintFactoryBean factoryBean = context.getBean(LogPrintFactoryBean.class);
        String log = factoryBean.getObject();
        System.out.println(log);
        context.close();
    }
}
