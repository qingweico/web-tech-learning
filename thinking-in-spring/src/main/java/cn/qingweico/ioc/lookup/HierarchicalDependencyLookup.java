package cn.qingweico.ioc.lookup;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 层次性依赖查找
 *
 * @author zqw
 * @date 2021/11/13
 */
public class HierarchicalDependencyLookup {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        HierarchicalBeanFactory parentFactory = createParentBeanFactory();
        beanFactory.setParentBeanFactory(parentFactory);
        context.register(HierarchicalDependencyLookup.class);

        displayContainsLocalBean(beanFactory);
        displayContainsLocalBean(parentFactory);

        displayContainsBean(beanFactory);
        displayContainsBean(parentFactory);
        context.refresh();
        context.close();
    }

    private static void displayContainsBean(HierarchicalBeanFactory beanFactory) {
        System.out.printf("当前 BeanFactory[ %s ] 是否包含 Bean[ %s ] : %s\n", beanFactory, "user",
                containsBean(beanFactory, "user"));
    }

    private static boolean containsBean(HierarchicalBeanFactory beanFactory, String beanName) {
        BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();
        if (parentBeanFactory instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory parentHierarchicalBeanFactory = (HierarchicalBeanFactory) parentBeanFactory;
            if (containsBean(parentHierarchicalBeanFactory, beanName)) {
                return true;
            }
        }
        return beanFactory.containsLocalBean(beanName);
    }

    private static void displayContainsLocalBean(HierarchicalBeanFactory beanFactory) {
        System.out.printf("当前 BeanFactory[ %s ] 是否包含 Local Bean[ %s ] : %s\n", beanFactory, "user",
                beanFactory.containsLocalBean("user"));
    }

    private static ConfigurableListableBeanFactory createParentBeanFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        String location = "META-INF/lookup-context.xml";
        reader.loadBeanDefinitions(location);
        return beanFactory;
    }
}
