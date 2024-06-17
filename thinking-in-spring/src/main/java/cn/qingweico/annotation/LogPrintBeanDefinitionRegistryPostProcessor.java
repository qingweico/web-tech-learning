package cn.qingweico.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.NonNull;

/**
 * 使用 BeanDefinitionRegistryPostProcessor
 * 在 Spring 容器启动时会执行 BeanDefinitionRegistryPostProcessor
 * 的 postProcessBeanDefinitionRegistry 方法, 可以进行 BeanDefinition 的注册操作
 *
 * @author zqw
 * @date 2022/9/24
 */
public class LogPrintBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(LogPrintConfig.class);
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        LogPrintBeanDefinitionRegistryPostProcessor bd = new LogPrintBeanDefinitionRegistryPostProcessor();
        context.addBeanFactoryPostProcessor(bd);
        context.refresh();
        String log = context.getBean("log", String.class);
        System.out.println(log);
        context.close();
    }
}
