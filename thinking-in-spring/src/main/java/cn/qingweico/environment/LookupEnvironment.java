package cn.qingweico.environment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

/**
 * 依赖注入 {@link Environment}
 *
 * @author zqw
 * @date 2022/7/2
 */
@Slf4j
public class LookupEnvironment implements EnvironmentAware {
    private Environment environment;


    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(LookupEnvironment.class);
        context.refresh();
        LookupEnvironment lookupEnvironment = context.getBean(LookupEnvironment.class);
        // 通过 Environment 的名称依赖查找
        Environment environment = context.getBean(ConfigurableApplicationContext.ENVIRONMENT_BEAN_NAME, Environment.class);

        // 无论是依赖查找还是依赖注入都是来源同一个 Environment 对象
        log.info("EnvironmentAware: [environment] == Lookup: [environment]: {}", environment == lookupEnvironment.environment);

        context.close();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
