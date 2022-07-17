package cn.qingweico.ioc.injection;

import cn.qingweico.ioc.annotation.InjectUser;
import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.context.annotation.AnnotationConfigUtils.AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME;


/**
 * @author zqw
 * @date 2021/11/15
 */
public class QualifierAnnotationDependency {
    @Autowired
    private User superUser;
    @Autowired
    @Qualifier("user")
    private User u;
    @Resource
    private User user;
    @Inject
    private User inject;
    @InjectUser
    private User injectUser;

    @Bean(name = AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)
    public static AutowiredAnnotationBeanPostProcessor beanPostProcessor() {
        AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        Set<Class<? extends Annotation>> autowiredAnnotationTypes =
                new LinkedHashSet<>(Arrays.asList(Autowired.class, Inject.class, InjectUser.class));
        autowiredAnnotationBeanPostProcessor.setAutowiredAnnotationTypes(autowiredAnnotationTypes);
        return autowiredAnnotationBeanPostProcessor;
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE - 3)
    public static AutowiredAnnotationBeanPostProcessor beanPostProcessorPrecedence() {
        AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        autowiredAnnotationBeanPostProcessor.setAutowiredAnnotationType(InjectUser.class);
        return autowiredAnnotationBeanPostProcessor;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(QualifierAnnotationDependency.class);
        UserHolder.loadUser(context);
        context.refresh();
        QualifierAnnotationDependency cls = context.getBean(QualifierAnnotationDependency.class);
        System.out.println(cls.superUser);
        System.out.println(cls.u);
        System.out.println(cls.user);
        System.out.println(cls.inject);
        System.out.println(cls.injectUser);
        context.close();
    }
}
