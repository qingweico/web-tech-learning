package cn.qingweico.bean.scope;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * @author zqw
 * @date 2021/11/21
 */
public class RegisterCustomScope {

    @Bean
    @Scope(ThreadLocalScope.SCOPE_NAME)
    public User user() {
        return createUser();
    }

    private User createUser() {
        User user = new User();
        user.setId(System.nanoTime());
        return user;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(RegisterCustomScope.class);
        context.addBeanFactoryPostProcessor(beanFactory -> beanFactory.registerScope(ThreadLocalScope.SCOPE_NAME, new ThreadLocalScope()));
        context.refresh();
        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(() -> {
                System.out.print("Thread id " + Thread.currentThread().getId() + ": ");
                beanScopeByLookup(context);
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print("Thread id " + Thread.currentThread().getId() + ": ");
        beanScopeByLookup(context);
        context.close();
    }

    private static void beanScopeByLookup(BeanFactory beanFactory) {
        User singleton = beanFactory.getBean("user", User.class);
        System.out.println("user: " + singleton);
    }
}
