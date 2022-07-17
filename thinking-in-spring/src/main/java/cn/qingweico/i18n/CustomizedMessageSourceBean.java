package cn.qingweico.i18n;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Spring Boot 场景下自定义 {@link MessageSource} Bean
 *
 * @author zqw
 * @date 2021/12/23
 * @see MessageSource
 * @see MessageSourceAutoConfiguration
 * @see ReloadableResourceBundleMessageSource
 */
@EnableAutoConfiguration
public class CustomizedMessageSourceBean {

    /// Spring Boot Primary Configuration Class 优先级高于 xxAutoConfiguration(即会被覆盖)
    @Bean(AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)
    public MessageSource messageSource() {
        return new ReloadableResourceBundleMessageSource();
    }
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(CustomizedMessageSourceBean.class)
                .web(WebApplicationType.NONE)
                .run(args);
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();

        String messageSourceBeanName = AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME;
        if(beanFactory.containsBean(messageSourceBeanName)) {
            // 查找 MessageSource 的 BeanDefinition
            System.out.println(beanFactory.getBeanDefinition(messageSourceBeanName));
            MessageSource messageSource = applicationContext.getBean(messageSourceBeanName, MessageSource.class);
            System.out.println(messageSource);
        }
        applicationContext.close();

    }
}
