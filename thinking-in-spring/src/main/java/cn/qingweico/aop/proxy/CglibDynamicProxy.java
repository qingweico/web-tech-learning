package cn.qingweico.aop.proxy;

import cn.qingweico.aop.service.DefaultEchoService;
import cn.qingweico.aop.service.EchoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author zqw
 * @date 2022/7/20
 */
@Slf4j
public class CglibDynamicProxy {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        Class<?> superClass = DefaultEchoService.class;
        enhancer.setSuperclass(superClass);
        // 指定拦截接口
        enhancer.setInterfaces(new Class[]{EchoService.class});
        enhancer.setCallback(new MethodInterceptor() {

            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                long start = System.currentTimeMillis();
                // 对象o是Cglib生成的子类
                Object result = methodProxy.invokeSuper(o, args);
                long cost = System.currentTimeMillis() - start;
                log.info("[CGLIB] cost time: {} ms", cost);
                return result;
            }

        });
        // 创建代理对象
        EchoService o = (EchoService) enhancer.create();
        System.out.println(o.echo("Cglib Dynamic Proxy"));
    }
}
