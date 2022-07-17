package cn.qingweico.event;

import org.springframework.context.support.GenericApplicationContext;

/**
 * @author zqw
 * @date 2022/1/25
 */
public class CustomizedSpringEvent {
    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.addApplicationListener(new ApplicationListenerExtension<ApplicationEventExtension>());
        applicationContext.refresh();
        applicationContext.publishEvent(new ApplicationEventExtension("customized Spring event"));
        applicationContext.close();
    }
}
