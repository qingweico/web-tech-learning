package cn.qingweico.aop.service;

/**
 * @author zqw
 * @date 2022/7/20
 */
public class DefaultEchoServiceImpl implements EchoService {
    @Override
    public String echo(String msg) {
        return "[Default ECHO]: " + msg;
    }
}
