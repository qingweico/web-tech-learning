package cn.qingweico.service;

/**
 * @author zqw
 * @date 2025/9/6
 */
public interface LoginPasswordDecoder {
    /**
     * 是否支持当前加密模式
     */
    boolean support(String mode);

    /**
     * 解码登录密码密文
     */
    String decode(String secretPassword);


}
