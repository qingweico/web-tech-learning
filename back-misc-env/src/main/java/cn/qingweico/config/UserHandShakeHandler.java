package cn.qingweico.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;

import java.util.Map;

/**
 * @author zqw
 * @date 2025/8/20
 */
public class UserHandShakeHandler implements HandshakeHandler {
    @Override
    public boolean doHandshake(@NotNull ServerHttpRequest serverHttpRequest,
                               @NotNull ServerHttpResponse serverHttpResponse,
                               @NotNull WebSocketHandler webSocketHandler,
                               @NotNull Map<String, Object> map) throws HandshakeFailureException {
        return true;
    }
}
