package cn.qingweico.aop.service;

/**
 * @author zqw
 * @date 2022/7/20
 */
public interface EchoService {
    /**
     * Echo Msg
     *
     * @param msg Message
     * @return Print msg
     * @throws IllegalArgumentException IllegalArgumentException
     */
    String echo(String msg) throws IllegalArgumentException;

    /**
     * print Object
     *
     * @param obj Object
     */

    default void print(Object obj) {
        System.out.print(obj + "\n");
    }
}
