package cn.qingweico.core.sdk.base;

import java.io.Serializable;

/**
 * 支持序列化的 Function
 * 为了获取字段名字
 *
 * @author zqw
 * @date 2023/11/04
 */
@FunctionalInterface
public interface InterFunction<T, R> extends Serializable {

    /**
     * T -> R
     *
     * @param t input parameter
     * @return R output parameter
     */
    R apply(T t);

}
