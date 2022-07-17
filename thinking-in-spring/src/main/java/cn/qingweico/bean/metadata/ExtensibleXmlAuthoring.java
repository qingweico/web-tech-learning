package cn.qingweico.bean.metadata;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author zqw
 * @date 2021/12/21
 */
public class ExtensibleXmlAuthoring {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("META-INF/user-extension.xml");
        User user = beanFactory.getBean(User.class);
        System.out.println(user);
    }
}
