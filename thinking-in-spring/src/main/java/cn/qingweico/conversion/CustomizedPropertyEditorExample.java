package cn.qingweico.conversion;

import cn.qingweico.ioc.domain.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zqw
 * @date 2021/12/30
 */
public class CustomizedPropertyEditorExample {
    public static void main(String[] args) {
        String configLocation = "classpath:META-INF/property-editor.xml";
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext();
        applicationContext.setConfigLocation(configLocation);
        applicationContext.refresh();

        // ConversionService 来源:
        // AbstractApplicationContext -> getBean("conversionService", ConversionService.class);
        // ConfigurableBeanFactory -> setConversionService(conversionService)
        // AbstractBeanFactory(extends ConfigurableBeanFactory)#getConversionService
        // BeanDefinition -> BeanWrapper -> 属性转换(数据来源: PropertyValues)
        // setPropertyValues(PropertyValues) -> TypeConverter#convertIfNecessary
        // TypeConverterDelegate#convertIfNecessary -> PropertyEditor or ConversionService

        // AbstractApplicationContext#finishBeanFactory
        // DefaultListableBeanFactory#doCreateBean
        // AbstractAutowireCapableBeanFactory#createBeanInstance
        // AbstractAutowireCapableBeanFactory#instantiateBean
        // AbstractBeanFactory#initBeanWrapper
        System.out.println(applicationContext.getBean("user", User.class));
        applicationContext.close();
    }
}
