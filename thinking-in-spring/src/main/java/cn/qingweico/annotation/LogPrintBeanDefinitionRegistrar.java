package cn.qingweico.annotation;

import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * @author zqw
 * @date 2022/9/23
 */
public class LogPrintBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry,
                                        @NonNull BeanNameGenerator importBeanNameGenerator) {
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(LogPrintConfig.class).getBeanDefinition();
        //  <=> AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(LogPrintConfig.class);
        registry.registerBeanDefinition("lpc", beanDefinition);
        // <=> BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }
}
