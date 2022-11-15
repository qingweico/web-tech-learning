package cn.qingweico.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * '@Enable*' 模块示例
 *
 * @author zqw
 * @date 2022/9/23
 */
@EnableLogPrint
public class EnableModule {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(EnableModule.class);
        context.refresh();
        String log = context.getBean("log", String.class);
        System.out.println(log);
        context.close();
    }
}
