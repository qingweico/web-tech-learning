package cn.qingweico.aop.service;

/**
 * @author zqw
 * @date 2022/7/20
 */
public interface EchoService {
    String echo(String msg) throws IllegalArgumentException;

    default void print(Object obj) {
        System.out.print(obj + "\n");
    }
}
