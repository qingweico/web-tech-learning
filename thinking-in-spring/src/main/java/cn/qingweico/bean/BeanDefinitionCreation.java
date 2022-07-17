package cn.qingweico.bean;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * 定义Bean的元信息
 *
 * @author zqw
 * @date 2021/11/10
 */
public class BeanDefinitionCreation {
    public static void main(String[] args) {
        // 通过 BeanDefinitionBuilder 创建
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .genericBeanDefinition(User.class);
        // 设置属性
        beanDefinitionBuilder.addPropertyValue("id", 1)
        .addPropertyValue("name", "li");
        // 获取 BeanDefinition 实例
        // 这里的 BeanDefinition 并非 Bean 的最终定义,还可以修改其属性
        System.out.println(beanDefinitionBuilder.getBeanDefinition());

        // 通过 AbstractBeanDefinition 以及派生类
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(User.class);
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
        mutablePropertyValues.add("id", 1)
                .add("name", "li");
        genericBeanDefinition.setPropertyValues(mutablePropertyValues);
       System.out.println(genericBeanDefinition);
    }
}
