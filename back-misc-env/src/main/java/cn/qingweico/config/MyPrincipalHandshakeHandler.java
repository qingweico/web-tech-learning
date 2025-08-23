package cn.qingweico.config;

import com.alibaba.excel.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

/**
 * @author zqw
 * @date 2025/8/20
 */
@Slf4j
public class MyPrincipalHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(@NotNull ServerHttpRequest request,
                                      @NotNull WebSocketHandler wsHandler,
                                      @NotNull Map<String, Object> attributes) {

        HttpSession httpSession = getSession(request);
        if (null == httpSession) {
            return null;
        }
        String user = (String) httpSession.getAttribute("loginName");

        if (StringUtils.isEmpty(user)) {
            log.error("websocket连接失败, 未登录系统");
            return null;
        }
        log.info(" MyDefaultHandshakeHandler login = {}", user);
        return new WebSocketUserAuthentication(user);
    }

    private HttpSession getSession(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest serverRequest) {
            return serverRequest.getServletRequest().getSession(false);
        }
        return null;
    }

}
