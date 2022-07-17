package cn.qingweico.bean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * --------------- Bean GC ---------------
 *
 * @author zqw
 * @date 2021/11/12
 */
public class BeanGc {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(BeanInitializationAndDestroy.class);
        applicationContext.refresh();
        applicationContext.close();
        System.gc();
    }
}
