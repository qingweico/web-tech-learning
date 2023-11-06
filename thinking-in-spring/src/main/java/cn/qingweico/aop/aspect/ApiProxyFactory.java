package cn.qingweico.aop.aspect;

import cn.qingweico.aop.interceptor.EchoServiceMethodInterceptor;
import cn.qingweico.aop.service.DefaultEchoServiceImpl;
import cn.qingweico.aop.service.EchoService;
import org.springframework.aop.framework.ProxyFactory;

/**
 * 基于 {@code ProxyFactory} 生成代理对象
 * @author zqw
 * @date 2022/7/23
 * {@link ProxyFactory}
 */
public class ApiProxyFactory {
    public static void main(String[] args) {
        DefaultEchoServiceImpl defaultEchoService = new DefaultEchoServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(defaultEchoService);
        proxyFactory.addAdvice(new EchoServiceMethodInterceptor());
        EchoService echoService = (EchoService)proxyFactory.getProxy();
        System.out.println(echoService.echo("ApiProxyFactory"));
    }
}
