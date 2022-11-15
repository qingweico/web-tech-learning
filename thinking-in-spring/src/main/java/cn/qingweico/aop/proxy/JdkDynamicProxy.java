package cn.qingweico.aop.proxy;


import cn.qingweico.aop.service.DefaultEchoService;
import cn.qingweico.aop.service.EchoService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

/**
 * @author zqw
 * @date 2022/7/20
 */
@Slf4j
public class JdkDynamicProxy {
    // 需要被代理的对象
    private Object needToProxiedObject;
    /**
     * 生成代理对象
     *
     * @return {@code Object} 代理对象
     */
    public Object newProxy(Object targetObject) {
        this.needToProxiedObject = targetObject;
        ClassLoader classLoader = needToProxiedObject.getClass().getClassLoader();
        // 生成代理对象
        return Proxy.newProxyInstance(classLoader, needToProxiedObject.getClass().getInterfaces(), (proxy, method, args) -> {
            if (EchoService.class.isAssignableFrom(method.getDeclaringClass())) {
                long start = System.currentTimeMillis();
                Object result = method.invoke(needToProxiedObject, args);
                long cost = System.currentTimeMillis() - start;
                log.info("[JdkDynamicProxy] cost time: {} ms", cost);
                return result;
            }
            return null;
        });
    }

    public static void main(String[] args) {
        JdkDynamicProxy jdkProxy = new JdkDynamicProxy();
        EchoService service = (EchoService) jdkProxy.newProxy(new DefaultEchoService());
        String result = service.echo("Jdk Dynamic Proxy");
        System.out.println(result);
        service.print("Jdk Dynamic Proxy");
    }
}
