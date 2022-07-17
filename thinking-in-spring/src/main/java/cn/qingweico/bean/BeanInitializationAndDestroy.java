package cn.qingweico.bean;

import cn.qingweico.bean.factory.DefaultUserFactory;
import cn.qingweico.bean.factory.UserFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 * --------------- Bean 初始化和销毁 ---------------
 * @author zqw
 * @date 2021/11/11
 */
public class BeanInitializationAndDestroy {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(BeanInitializationAndDestroy.class);
        applicationContext.refresh();
        System.out.println("Spring 应用上下文已启动...");
        // 延迟初始化: Spring容器启动后并不会初始化,依赖查找才触发了初始化(按需初始化)
        // 非延迟初始化: Spring容器启动就会初始化
        applicationContext.getBean(UserFactory.class);
        // close()方法出发了Bean的销毁
        applicationContext.close();
    }

    @Bean(initMethod = "initUserFactory", destroyMethod = "doDestroy")
    @Lazy(value = false)
    public DefaultUserFactory userFactory() {
        return new DefaultUserFactory();
    }
}
