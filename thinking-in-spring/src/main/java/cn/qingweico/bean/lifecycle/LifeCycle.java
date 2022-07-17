package cn.qingweico.bean.lifecycle;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.Lifecycle;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author zqw
 * @date 2022/3/12
 */
public class LifeCycle {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBeanDefinition("lifecycle", BeanDefinitionBuilder.
                rootBeanDefinition(LifeCycleImpl.class).getBeanDefinition());
        context.refresh();
        context.start();
        context.stop();
        context.close();
    }

    static class LifeCycleImpl implements Lifecycle {
        private boolean running = false;

        @Override
        public void start() {
            this.running = true;
            System.out.println("LifeCycleImpl start...");
        }

        @Override
        public void stop() {
            this.running = false;
            System.out.println("LifeCycleImpl stop...");
        }

        @Override
        public boolean isRunning() {
            return running;
        }
    }
}
