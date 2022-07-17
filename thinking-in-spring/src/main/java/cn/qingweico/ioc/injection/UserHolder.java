package cn.qingweico.ioc.injection;

import cn.qingweico.bean.BeanAlias;
import cn.qingweico.ioc.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Calendar;

/**
 * @author zqw
 * @date 2021/11/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHolder implements BeanNameAware,
        BeanClassLoaderAware,
        BeanFactoryAware,
        EnvironmentAware,
        InitializingBean,
        SmartInitializingSingleton,
        DisposableBean {
    private User user;

    private String number;

    private String desc;

    public static void loadUser(BeanDefinitionRegistry context) {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        String location = "META-INF/lookup-context.xml";
        // 加载 XML 资源 解析并且生成 BeanDefinition
        reader.loadBeanDefinitions(location);
    }

    public UserHolder(User user) {
        this.user = user;
    }

    @PostConstruct
    public void initPostConstruct() {
        this.desc = "this is V3.0 desc";
        System.out.println("initPostConstruct...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.desc = "this is V4.0 desc";
        System.out.println("afterPropertiesSet...");
    }

    public void init() {
        this.desc = "this is V5.0 desc";
        System.out.println("自定义init...");
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.desc = "this is V7.0 desc";
        System.out.println("afterSingletonsInstantiated...");
    }

    private ClassLoader classLoader;
    private BeanFactory beanFactory;
    private String beanName;
    private Environment environment;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    @PreDestroy
    public void preDestroy() {
        this.desc = "this is V9.0 desc";
        System.out.println("preDestroy...");
    }
    @Override
    public void destroy() throws Exception {
        this.desc = "this is V10.0 desc";
        System.out.println("destroy...");
    }
    public void doDestroy() {
        this.desc = "this is V11.0 desc";
        System.out.println("自定义destroy...");
    }
}
