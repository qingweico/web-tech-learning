package cn.qingweico.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author zqw
 * @date 2022/1/25
 */
public class HierarchicalSpringEventPropagate {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext();
        parentContext.setId("parent-context");
        parentContext.register(ApplicationContextEventListener.class);

        AnnotationConfigApplicationContext currentContext = new AnnotationConfigApplicationContext();
        currentContext.setId("current-context");
        currentContext.setParent(parentContext);
        currentContext.register(ApplicationContextEventListener.class);
        parentContext.refresh();
        currentContext.refresh();

        currentContext.close();
        parentContext.close();

    }

    static class ApplicationContextEventListener implements ApplicationListener<ApplicationContextEvent> {
        private static final Set<ApplicationContextEvent> processed = new LinkedHashSet<>();

        @Override
        public void onApplicationEvent(ApplicationContextEvent event) {
            if (processed.add(event)) {
                System.out.printf("[ ID: %s] [Event: %s]%n", event.getApplicationContext().getId(),
                        event.getClass().getSimpleName());
            }

        }
    }

}
