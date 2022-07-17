package cn.qingweico.bean;

import cn.qingweico.ioc.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * --------------- 注册 Bean  ---------------
 * 注解 注册 Bean {@link Component}  {@link Import} {@link Bean}
 * Bean 的命名 {@link AnnotationBeanNameGenerator}
 * 以配置类方式注册 Bean {@link AnnotatedBeanDefinitionReader}
 * @author zqw
 * @date 2021/11/10
 */
@Slf4j
@Import(RegisterBeanDefinition.Config.class)
public class RegisterBeanDefinition {
   public static void main(String[] args) {
      AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
      applicationContext.register(RegisterBeanDefinition.class);

      // 通过 BeanDefinition 注册 API 实现
      // 命名 Bean
      registerUserBeanDefinition(applicationContext, "naming-user");
      // 非命名 Bean
      registerUserBeanDefinition(applicationContext);
      applicationContext.refresh();
      log.info("Config 类型的所有 Beans: {}", applicationContext.getBeansOfType(Config.class));
      log.info("User 类型的所有 Beans: {}", applicationContext.getBeansOfType(User.class));
      applicationContext.close();
   }

   /**
    * 命名 Bean 的注册方式
    * @param registry registry
    * @param beanName beanName
    */
   public static void registerUserBeanDefinition(BeanDefinitionRegistry registry, String beanName) {
      BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
      beanDefinitionBuilder.addPropertyValue("id", 1)
              .addPropertyValue("name", "zhou");
      if(StringUtils.hasText(beanName)) {
         // 注册 BeanDefinition
         // TODO
         registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());

      }else {
         // 非命名 Bean 的注册方式
         // BeanDefinitionReaderUtils#uniqueBeanName
         BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinitionBuilder.getBeanDefinition(), registry);
      }
   }
   public static void registerUserBeanDefinition(BeanDefinitionRegistry registry) {
      registerUserBeanDefinition(registry, null);
   }
   @Component
   public static class Config {
      // alias-name 是别名,并不是真真正正的 Bean 的名称
      @Bean(name = {"user", "alias-user"})
      public User user() {
         User user = new User();
         user.setId(1L);
         user.setName("zhou");
         return user;
      }
   }
}
