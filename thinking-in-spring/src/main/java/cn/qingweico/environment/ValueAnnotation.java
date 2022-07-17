package cn.qingweico.environment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * @author zqw
 * @date 2022/7/2
 */
public class ValueAnnotation {

    @Value("${user.name}")
    private String username;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ValueAnnotation.class);
        context.refresh();
        // DefaultListableBeanFactory#doResolveDependency
        // QualifierAnnotationAutowireCandidateResolver#getSuggestedValue
        ValueAnnotation va = context.getBean(ValueAnnotation.class);
        System.out.println(va.username);
        context.close();
    }
}
