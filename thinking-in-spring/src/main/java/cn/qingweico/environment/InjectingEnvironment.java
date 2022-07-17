package cn.qingweico.environment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

/**
 * 依赖出入 {@link Environment}
 *
 * @author zqw
 * @date 2022/7/2
 */
@Slf4j
public class InjectingEnvironment implements EnvironmentAware, ApplicationContextAware {
    private Environment environment;
    private ApplicationContext applicationContext;
    @Autowired
    private Environment autowiredEnvironment;
    @Autowired
    private ApplicationContext autowiredApplicationContext;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(InjectingEnvironment.class);
        context.refresh();
        InjectingEnvironment injectingEnvironment = context.getBean(InjectingEnvironment.class);

        log.info("environment: {}", injectingEnvironment.environment);
        log.info("environment == autowiredEnvironment: {}",
                injectingEnvironment.environment == injectingEnvironment.autowiredEnvironment);
        log.info("environment == context.getEnvironment(): {}",
                injectingEnvironment.environment == context.getEnvironment());

        log.info("environment == applicationContext.getEnvironment(): {}",
                injectingEnvironment.environment == injectingEnvironment.applicationContext.getEnvironment());
        log.info("applicationContext == autowiredApplicationContext: {}",
                injectingEnvironment.applicationContext == injectingEnvironment.autowiredApplicationContext);
        log.info("applicationContext == context: {}",
                injectingEnvironment.applicationContext == context);
        context.close();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
