package cn.qingweico.bean;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zqw
 * @date 2021/11/10
 */
public class BeanAlias {
   private static final BeanFactory context = new
           ClassPathXmlApplicationContext("META-INF/bean-definition-context.xml");

   public static void main(String[] args) {
      User user = context.getBean("user", User.class);
      User alias = context.getBean("alias-user", User.class);
      // true
      System.out.println(user == alias);

   }
}
