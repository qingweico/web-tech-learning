package cn.qingweico.bean;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 控制 Bean 的初始化顺序
 *
 * @author zqw
 * @date 2023/5/7
 * @see org.springframework.core.annotation.Order
 */
public class SpringApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.addBeanFactoryPostProcessor(new SpringApplicationContextBeanFactoryPostProcessor());
    }
}
