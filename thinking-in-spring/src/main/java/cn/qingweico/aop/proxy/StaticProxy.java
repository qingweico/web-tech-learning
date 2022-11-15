package cn.qingweico.aop.proxy;

import cn.qingweico.aop.service.DefaultEchoService;
import cn.qingweico.aop.service.EchoService;
import cn.qingweico.aop.service.ProxyEchoService;

/**
 * @author zqw
 * @date 2022/7/20
 */
public class StaticProxy {
    public static void main(String[] args) {
        EchoService echoService = new ProxyEchoService(new DefaultEchoService());
        String result = echoService.echo("static proxy");
        System.out.println(result);
    }
}
