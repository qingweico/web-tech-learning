package cn.qingweico.bean.lifecycle;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;

/**
 * @author zqw
 * @date 2021/11/21
 */
public class AnnotatedBeanDefinitionParse {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 基于注解的(AnnotatedBeanDefinitionReader)Bean元信息配置
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
        int beanDefinitionCountBeforeRegister = beanFactory.getBeanDefinitionCount();
        reader.register(AnnotatedBeanDefinitionParse.class);
        int beanDefinitionCountAfterRegister = beanFactory.getBeanDefinitionCount();
        int beanDefinitionCount = beanDefinitionCountAfterRegister - beanDefinitionCountBeforeRegister;
        System.out.println(beanDefinitionCount);
        // 普通的 Class 作为 Component 注入到 Spring IOC 容器后 名称 大驼峰 -> 小驼峰
        // Bean 的名称生成来自于 BeanNameGenerator, 注解实现: AnnotationBeanNameGenerator
        AnnotatedBeanDefinitionParse cls = beanFactory.getBean("annotatedBeanDefinitionParse"
                , AnnotatedBeanDefinitionParse.class);
        System.out.println(cls.getClass());
    }
}
