package cn.qingweico.aop.service;

/**
 * @author zqw
 * @date 2022/7/24
 */
public class OtherEchoServiceImpl implements EchoService{
    @Override
    public String echo(String msg) throws IllegalArgumentException {
        return "[Other ECHO]: " + msg;
    }
}
