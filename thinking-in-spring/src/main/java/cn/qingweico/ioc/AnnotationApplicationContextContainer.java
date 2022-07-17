package cn.qingweico.ioc;

import cn.qingweico.ioc.domain.User;
import cn.qingweico.ioc.lookup.DependencyLookup;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * {@link ApplicationContext} 作为 IOC 容器
 *
 * @author zqw
 * @date 2021/11/8
 */
public class AnnotationApplicationContextContainer {
    public static void main(String[] args) {
        // 创建ApplicationContext容器
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AnnotationApplicationContextContainer.class);
        // 启动Spring应用上下文
        applicationContext.refresh();
        DependencyLookup.lookupCollectionByType(applicationContext);
    }

    @Bean
    public User user() {
        User user = new User();
        user.setId(1L);
        user.setName("li");
        return user;
    }
}
