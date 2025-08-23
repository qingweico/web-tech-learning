package cn.qingweico.config;

import java.security.Principal;

/**
 * @author zqw
 * @date 2025/8/20
 */
public class WebSocketUserAuthentication implements Principal {
    /**
     * 用户身份标识符
     */
    private final String token;

    public WebSocketUserAuthentication(String token) {
        this.token = token;
    }


    @Override
    public String getName() {
        return token;
    }

}
