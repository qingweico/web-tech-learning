package cn.qingweico.bean.lifecycle;

import cn.qingweico.ioc.domain.SuperUser;
import cn.qingweico.ioc.domain.User;
import cn.qingweico.ioc.injection.UserHolder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author zqw
 * @date 2021/11/22
 */
public class BeanDefinitionMerge {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        UserHolder.loadUser(beanFactory);

        // RootBeanDefinition 不需要合并; 没有parent

        // user 和 superUser 初始化都为 GenericBeanDefinition; 经过合并后变为 RootBeanDefinition
        User user = beanFactory.getBean("user", User.class);
        System.out.println(user);
        // superUser(GenericBeanDefinition) 合并 -> RootBeanDefinition 覆盖了parent 相关属性
        SuperUser superUser = beanFactory.getBean("superUser", SuperUser.class);
        System.out.println(superUser);
    }
}