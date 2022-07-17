package cn.qingweico.bean;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zqw
 * @date 2021/11/14
 */
public class BeanInsException {
   public static void main(String[] args) {
      AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
      BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CharSequence.class);

      // BeanInstantiationException
      context.registerBeanDefinition("charSequence", builder.getBeanDefinition());
      context.refresh();
      context.close();
   }
}
