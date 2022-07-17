package cn.qingweico.ioc.injection;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * @author zqw
 * @date 2021/11/15
 */
public class ApiDependencySetter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 生成 UserHolder 的 BeanDefinition
        BeanDefinition userHolderBeanDefinition = createUserHolderBeanDefinition();
        // 注册 UserHolder 的 BeanDefinition
        context.registerBeanDefinition("userHolder", userHolderBeanDefinition);
        UserHolder.loadUser(context);
        context.refresh();
        UserHolder userHolder = context.getBean(UserHolder.class);
        System.out.println(userHolder);
        context.close();
    }

    public static BeanDefinition createUserHolderBeanDefinition() {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(UserHolder.class);
        // name ---> ref=""
        builder.addPropertyReference("user", "user");
        // constructor
        // builder.addConstructorArgReference("user");
        return builder.getBeanDefinition();


    }
}
