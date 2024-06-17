package cn.qingweico.annotation;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 激活 日志打印模块
 * 几种将 Bean 放入 Spring 容器的方式
 * 1: @Configuration + @Bean {@link ConfigurationBean}
 * 2: @Component + @ComponentScan {@link ComponentScanBean}
 * 3: @Import 注解
 * 4: FactoryBean 接口 {@link LogPrintFactoryBean}
 * 5: BeanDefinitionRegistryPostProcessor 接口 {@link LogPrintBeanDefinitionRegistryPostProcessor}
 * {@link DeferredImportSelector} 与 Spring Boot 中 自动导入配置文件 延迟导入有关
 *
 * @author zqw
 * @date 2022/9/23
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
// @Import(LogPrintConfig.class) 使用 @Import 注解
// @Import(LogPrintImportSelector.class) 使用 @Import + ImportSelector 接口
@Import(LogPrintBeanDefinitionRegistrar.class) // 使用 @Import + ImportBeanDefinitionRegistrar 接口
// @Import(LogPrintDeferredImportSelector.class) // 使用 @Import + DeferredImportSelector 接口
public @interface EnableLogPrint {
}
