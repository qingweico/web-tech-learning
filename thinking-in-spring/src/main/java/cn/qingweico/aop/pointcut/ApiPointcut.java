package cn.qingweico.aop.pointcut;

import cn.qingweico.aop.interceptor.EchoServiceMethodInterceptor;
import cn.qingweico.aop.service.DefaultEchoServiceImpl;
import cn.qingweico.aop.service.EchoService;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * @author zqw
 * @date 2022/7/23
 */
public class ApiPointcut {
    public static void main(String[] args) {

        EchoServicePointcut pointcut = new EchoServicePointcut("echo", EchoService.class);
        // 通过组合模式
        ComposablePointcut composablePointcut = new ComposablePointcut(EchoServiceEchoMethodPointcut.INSTANCE);
        composablePointcut.intersection(pointcut.getClassFilter());
        composablePointcut.intersection(pointcut.getMethodMatcher());
        DefaultEchoServiceImpl defaultEchoService = new DefaultEchoServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(defaultEchoService);
        // 将 Pointcut 适配成 Advisor
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new EchoServiceMethodInterceptor());
        proxyFactory.addAdvisor(advisor);
        // 使用 DefaultEchoService 不行
        EchoService proxy = (EchoService) proxyFactory.getProxy();
        System.out.println(proxy.echo("ApiPointcut"));
    }
}
