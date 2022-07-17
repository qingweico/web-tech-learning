package cn.qingweico.bean.lifecycle;

import cn.qingweico.ioc.domain.SuperUser;
import cn.qingweico.ioc.domain.User;
import cn.qingweico.ioc.injection.UserHolder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author zqw
 * @date 2021/11/22
 */
public class BeanInstantiation {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.addBeanPostProcessor(new InstantiationAwareBeanPostProcessorImpl());
        UserHolder.loadUser(beanFactory);
        User user = beanFactory.getBean("user", User.class);
        System.out.println(user);
        SuperUser superUser = beanFactory.getBean("superUser", SuperUser.class);
        System.out.println(superUser);
        UserHolder userHolder = beanFactory.getBean("userHolder", UserHolder.class);
        System.out.println(userHolder);
    }

}
