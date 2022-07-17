package cn.qingweico.resource;

import cn.qingweico.resource.util.ResourceUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

/**
 * @author zqw
 * @date 2021/12/22
 */
public class ResourceInjection {

    @Value("classpath:META-INF/user.properties")
    public Resource resource;

    @Value("classpath*:META-INF/*.properties")
    public Resource[] propertiesResources;

    @Value("${user.dir}")
    public String currentProjectPath;
    @PostConstruct
    public void init() {
        System.out.println(ResourceUtil.getResourceContext(resource));
        Stream.of(propertiesResources).map(ResourceUtil::getResourceContext).forEach(System.out::println);
        System.out.println(currentProjectPath);
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ResourceInjection.class);
        context.refresh();
        context.close();
    }
}
