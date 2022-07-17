package cn.qingweico.ioc.injection;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author zqw
 * @date 2021/11/15
 */
public class XmlDependencySetter {
    // 使用 xml 资源进行 setter 方法依赖注入
    public static void main(String[] args) {
        DefaultListableBeanFactory listableBeanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(listableBeanFactory);
        String location = "META-INF/injection-setter-context.xml";
        // 加载 XML 资源 解析并且生成 BeanDefinition
       reader.loadBeanDefinitions(location);
       UserHolder userHolder = listableBeanFactory.getBean(UserHolder.class);
        System.out.println(userHolder);
    }
}
