package cn.qingweico.aop.utils;

import org.springframework.aop.framework.AopProxyUtils;

/**
 * 获取真实的对象 而非代理对象
 *
 * @author zqw
 * @date 2023/9/21
 */
public class AcquireRealObject {
    public static void main(String[] args) {
        AopProxyUtils.getSingletonTarget(null);
    }
}
