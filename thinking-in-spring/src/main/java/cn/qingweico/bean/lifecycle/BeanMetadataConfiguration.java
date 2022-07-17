package cn.qingweico.bean.lifecycle;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;


/**
 * @author zqw
 * @date 2021/11/21
 */
public class BeanMetadataConfiguration {
   public static void main(String[] args) {
      DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
      // 基于 Properties 资源的Bean元信息配置
      PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(beanFactory);
      String location = "META-INF/user.properties";
      Resource resource = new ClassPathResource(location);
      EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
      reader.loadBeanDefinitions(encodedResource);
      User user = beanFactory.getBean("user", User.class);
      System.out.println(user);
   }
}
