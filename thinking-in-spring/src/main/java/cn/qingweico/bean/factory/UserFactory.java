package cn.qingweico.bean.factory;

import cn.qingweico.ioc.domain.User;

/**
 * --------------- User 工厂类 ---------------
 * @author zqw
 * @date 2021/11/11
 */
public interface UserFactory {
    /**
     * 创建 User 对象
     * @return {@link User}
     */
    default User createUser() {
        return User.createUser();
    }
}
