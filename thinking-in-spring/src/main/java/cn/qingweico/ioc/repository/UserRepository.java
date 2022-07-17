package cn.qingweico.ioc.repository;

import cn.qingweico.ioc.domain.User;
import lombok.Data;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;

import java.util.Collection;

/**
 * @author zqw
 * @date 2021/11/8
 */
@Data
public class UserRepository {
    private Collection<User> users;
    private BeanFactory beanFactory;
    private ObjectFactory<ApplicationContext> objectFactory;
}
