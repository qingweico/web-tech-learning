package cn.qingweico.aop.advisor;

import cn.qingweico.aop.service.EchoService;
import org.springframework.aop.IntroductionAdvisor;
import org.springframework.aop.IntroductionInfo;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultIntroductionAdvisor;

import java.lang.reflect.Method;

/**
 * {@link IntroductionAdvisor} 示例
 *
 * @author zqw
 * @date 2022/9/25
 */
public class SpringIntroductionAdvisorImpl implements EchoService {
    public static void main(String[] args) {
        SpringIntroductionAdvisorImpl sia = new SpringIntroductionAdvisorImpl();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(sia);
        proxyFactory.addAdvisor(new DefaultIntroductionAdvisor(new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
                System.out.println("MethodBeforeAdvice: " + method);
            }
        }, new IntroductionInfo() {
            @Override
            public Class<?>[] getInterfaces() {
                return new Class[]{EchoService.class};
            }
        }));
        Object proxy = proxyFactory.getProxy();
        EchoService service = (EchoService)proxy;
        service.echo("introduction advisor");
    }

    @Override
    public String echo(String msg) throws IllegalArgumentException {
        return "SpringIntroductionAdvisor: " + msg;
    }

}

