package cn.qingweico.aop.aspect;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用 API 创建 @AspectJ 代理
 *
 * @author zqw
 * @date 2022/7/23
 * {@link AspectJProxyFactory}
 */
public class ApiAspectJProxyFactory {
    public static void main(String[] args) {
        Map<String, Object> cache = new HashMap<>();
        // 设置需要被代理的对象
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(cache);
        // 增加 Aspect 配置类
        proxyFactory.addAspect(AspectConfig.class);
        proxyFactory.addAdvice(new MethodBeforeAdvice() {
            @Override
            public void before(@NonNull Method method, @NonNull Object[] args, Object target) throws Throwable {
                if ("put".equals(method.getName()) && args.length == 2) {
                    System.out.printf("当前存放的 Key 是 %s 值是 %s %n", args[0], args[1]);
                }
            }
        });
        // 添加 AfterReturningAdvice
        proxyFactory.addAdvice(new AfterReturningAdvice() {
            @Override
            public void afterReturning(Object returnValue, @NonNull Method method, @NonNull Object[] args, Object target) {
                if ("put".equals(method.getName()) && args.length == 2) {
                    System.out.printf("Map put 返回值为 %s%n", returnValue);
                }
            }
        });

        // 通过代理对象储存数据
        Map<String, Object> proxy = proxyFactory.getProxy();
        proxy.put("k1", "v1");
        proxy.put("k2", "v2");
        System.out.println(proxy.get("k1"));
    }
}
