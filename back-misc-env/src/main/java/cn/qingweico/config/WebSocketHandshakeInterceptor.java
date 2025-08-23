package cn.qingweico.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * @author zqw
 * @date 2025/8/20
 */
public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request,
                                   @NotNull ServerHttpResponse response,
                                   @NotNull WebSocketHandler webSocketHandler,
                                   @NotNull Map<String, Object> map)
            throws Exception {
        return super.beforeHandshake(request, response, webSocketHandler, map);
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest serverHttpRequest,
                               @NotNull ServerHttpResponse serverHttpResponse,
                               @NotNull WebSocketHandler webSocketHandler, Exception e) {
    }
}
