package cn.qingweico.ioc.domain;

import cn.qingweico.enums.City;
import lombok.Data;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.core.io.Resource;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Properties;

/**
 * @author zqw
 * @date 2021/11/7
 */
@Data
public class User implements BeanNameAware {
    private Long id;
    private String name;
    private City city;
    private Company company;
    private Resource configFileLocation;
    private City[] workCities;
    private List<City> lifeCities;
    private Properties address;
    private Properties addressToText;

    /**
     * 当前 Bean 的名称
     */
    private transient String beanName;

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("zhou");
        return user;
    }

    @PostConstruct
    public void init() {
        System.out.println("User Bean [ " + beanName + " ] init...");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("User Bean [ " + beanName + " ] destroy...");
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
