package cn.qingweico.bean.factory;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author zqw
 * @date 2022/7/10
 */
@Component
@PropertySource(value = "classpath:META-INF/user.properties", encoding = "UTF-8")
public class CustomizedUserFactory implements UserFactory{
    @Value("${user.id}")
    private long id;
    @Value("${user.name}")
    private String name;
    @Override
    public User createUser() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }
}
