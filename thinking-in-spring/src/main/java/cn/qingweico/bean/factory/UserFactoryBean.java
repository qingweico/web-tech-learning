package cn.qingweico.bean.factory;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.FactoryBean;

/**
 * --------------- User Bean 的 {@link FactoryBean} 的实现 ---------------
 * @author zqw
 * @date 2021/11/11
 */
public class UserFactoryBean implements FactoryBean<User> {

    @Override
    public User getObject() {
        return User.createUser();
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }
}
