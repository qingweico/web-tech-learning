package cn.qingweico.ioc.dependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.PostConstruct;

/**
 * @author zqw
 * @date 2021/11/19
 */
public class ResolvableDependencyResource {
    @Autowired
    private String value;

    @PostConstruct
    private void init() {
        System.out.println(value);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ResolvableDependencyResource.class);
        context.addBeanFactoryPostProcessor((beanFactory) -> {
            // 回调注册 Resolvable Dependency
            beanFactory.registerResolvableDependency(String.class, "hello");
        });
        context.refresh();
        context.close();
    }
}
