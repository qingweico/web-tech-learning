package cn.qingweico.bean;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * --------------- 常规方式实例化 Bean ---------------
 *
 * @author zqw
 * @date 2021/11/11
 */
public class BeanInstantiation {
    static BeanFactory context = new
            ClassPathXmlApplicationContext("META-INF/bean-instantiation-context.xml");
    public static void main(String[] args) {
        User var1 = context.getBean("user-by-static-method", User.class);
        User var2 = context.getBean("user-by-instance-method", User.class);
        User var3 = context.getBean("user-by-factory-bean", User.class);
        System.out.println(var1);
        System.out.println(var2);
        System.out.println(var3);
        System.out.println(var1 == var2);
        System.out.println(var1 == var3);
    }
}
