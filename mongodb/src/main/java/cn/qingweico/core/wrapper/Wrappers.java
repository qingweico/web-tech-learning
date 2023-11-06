package cn.qingweico.core.wrapper;

/**
 * Wrapper 条件构造
 *
 * @author zqw
 * @date 2023/11/04
 */
public final class Wrappers {

    public static <T> LambdaQueryWrapper<T> lambdaQuery() {
        return new LambdaQueryWrapper<>();
    }

}
