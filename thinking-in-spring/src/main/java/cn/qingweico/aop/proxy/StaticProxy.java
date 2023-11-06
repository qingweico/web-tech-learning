package cn.qingweico.aop.proxy;

import cn.qingweico.aop.service.DefaultEchoServiceImpl;
import cn.qingweico.aop.service.EchoService;
import cn.qingweico.aop.service.ProxyEchoServiceImpl;

/**
 * 静态代理 使用组合的方式
 *
 * @author zqw
 * @date 2022/7/20
 */
public class StaticProxy {
    public static void main(String[] args) {
        EchoService echoService = new ProxyEchoServiceImpl(new DefaultEchoServiceImpl());
        String result = echoService.echo("static proxy");
        System.out.println(result);
    }
}
