package cn.qingweico.bean.lifecycle;

import cn.qingweico.ioc.injection.UserHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.util.ObjectUtils;

/**
 * @author zqw
 * @date 2021/12/19
 */
public class DestructionAwareBeanPostProcessorImpl implements DestructionAwareBeanPostProcessor {
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        if (ObjectUtils.nullSafeEquals("userHolder", beanName) && UserHolder.class.equals(bean.getClass())) {
            UserHolder userHolder = (UserHolder) bean;
            userHolder.setDesc("this is a V8.0 desc");
            System.out.println("postProcessBeforeDestruction...");
        }
    }
}
