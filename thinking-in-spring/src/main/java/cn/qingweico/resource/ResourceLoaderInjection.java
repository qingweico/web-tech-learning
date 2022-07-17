package cn.qingweico.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;

/**
 * @author zqw
 * @date 2021/12/22
 */
public class ResourceLoaderInjection implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;
    @Autowired
    private ResourceLoader autoAiredResourceLoader;

    @Autowired
    private AbstractApplicationContext abstractApplicationContext;

    @PostConstruct
    public void init() {

        // ApplicationContextAwareProcessor#invokeAwareInterfaces
        System.out.println(resourceLoader == autoAiredResourceLoader);

        // AbstractApplicationContext#prepareBeanFactory
        System.out.println(autoAiredResourceLoader == abstractApplicationContext);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ResourceLoaderInjection.class);
        context.refresh();
        context.close();
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
