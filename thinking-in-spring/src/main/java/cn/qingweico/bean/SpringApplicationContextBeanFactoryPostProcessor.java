package cn.qingweico.bean;

import cn.qingweico.annotation.LogPrintConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * @author zqw
 * @date 2023/5/7
 */
public class SpringApplicationContextBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
         AbstractBeanDefinition abd = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
        abd.setBeanClass(LogPrintConfig.class);
        registry.registerBeanDefinition("logPrintConfig", abd);

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
