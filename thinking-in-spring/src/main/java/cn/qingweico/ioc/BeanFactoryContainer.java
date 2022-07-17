package cn.qingweico.ioc;

import cn.qingweico.ioc.lookup.DependencyLookup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * {@link BeanFactory} 作为 IOC 容器
 *
 * @author zqw
 * @date 2021/11/8
 */
@Slf4j
public class BeanFactoryContainer {
   public static void main(String[] args) {
      // 创建BeanFactory容器
      DefaultListableBeanFactory listableBeanFactory = new DefaultListableBeanFactory();
      XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(listableBeanFactory);
      String location = "META-INF/lookup-context.xml";
      int beanDefinitionCount = reader.loadBeanDefinitions(location);
      log.info("beanDefinitionCount: {}", beanDefinitionCount);
      DependencyLookup.lookupCollectionByType(listableBeanFactory);
   }
}
