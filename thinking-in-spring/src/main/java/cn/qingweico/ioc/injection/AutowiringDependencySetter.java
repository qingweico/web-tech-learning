package cn.qingweico.ioc.injection;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;


/**
 * @author zqw
 * @date 2021/11/15
 */
public class AutowiringDependencySetter {
    public static void main(String[] args) {
        DefaultListableBeanFactory listableBeanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(listableBeanFactory);
        String location = "META-INF/autowire-setter-injection-context.xml";
        // 加载 XML 资源 解析并且生成 BeanDefinition
        reader.loadBeanDefinitions(location);
        UserHolder userHolder = listableBeanFactory.getBean(UserHolder.class);
        System.out.println(userHolder);
    }
}
