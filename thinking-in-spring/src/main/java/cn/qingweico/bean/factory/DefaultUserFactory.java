package cn.qingweico.bean.factory;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * --------------- UserFactory 默认实现 ---------------
 * @author zqw
 * @date 2021/11/11
 */
@SuppressWarnings({"unused"})
public class DefaultUserFactory implements UserFactory, InitializingBean, DisposableBean {

    // ################################ Bean 初始化方法 ################################


    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct: UserFactory init...");
    }

    public void initUserFactory() {
        System.out.println("自定义: UserFactory init...");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("InitializingBean#afterPropertiesSet(): UserFactory init...");
    }

    // ################################ Bean 销毁方法 ################################

    @PreDestroy
    public void preDestroy() {
        System.out.println("@PreDestroy: UserFactory destroy...");
    }

    @Override
    public void destroy() {
        System.out.println("DisposableBean#destroy(): UserFactory destroy...");
    }

    public void doDestroy() {
        System.out.println("自定义: UserFactory UserFactory...");
    }

    @Override
    protected void finalize() {
        System.out.println("DefaultUserFactory finalize...");
    }
}
