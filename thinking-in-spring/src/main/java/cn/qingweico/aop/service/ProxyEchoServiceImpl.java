package cn.qingweico.aop.service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zqw
 * @date 2022/7/20
 */
@Slf4j
public class ProxyEchoServiceImpl implements EchoService {
    private final EchoService echoService;

    public ProxyEchoServiceImpl(EchoService echoService) {
        this.echoService = echoService;
    }

    @Override
    public String echo(String msg) {
        long start = System.currentTimeMillis();
        String result = echoService.echo(msg);
        long cost = System.currentTimeMillis() - start;
        log.info("[StaticProxy] cost time: {} ms", cost);
        return result;
    }
}
