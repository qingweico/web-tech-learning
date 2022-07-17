package cn.qingweico.bean.lifecycle;

import cn.qingweico.ioc.injection.UserHolder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;

/**
 * @author zqw
 * @date 2021/12/19
 */
public class BeanDestroy {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.addBeanPostProcessor(new DestructionAwareBeanPostProcessorImpl());
        beanFactory.addBeanPostProcessor(new CommonAnnotationBeanPostProcessor());
        UserHolder.loadUser(beanFactory);
        UserHolder userHolder = beanFactory.getBean("userHolder", UserHolder.class);
        beanFactory.destroyBean("userHolder", userHolder);
        System.out.println(userHolder);
    }
}
