package cn.qingweico.validation;

import lombok.Data;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author zqw
 * @date 2021/12/28
 * @see Validator
 * @see LocalValidatorFactoryBean
 */
public class SpringBeanValidation {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:META-INF/bean-validator.xml");
        applicationContext.getBean(Validator.class);
        UserProcessor userProcessor = applicationContext.getBean(UserProcessor.class);
        userProcessor.process(new User());
        applicationContext.close();

    }

    @Component
    @Validated
    static class UserProcessor {
        public void process(@Valid User user) {
            System.out.println(user);
        }
    }

    @Data
    static class User {
        @NotNull
        private String name;
    }
}
