package cn.qingweico.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

/**
 * websocket事件监听
 *
 * @author zqw
 * @date 2025/8/20
 */
@Slf4j
@Component
public class WebSocketEventListener {

    @EventListener
    public void handleConnectListener(SessionConnectedEvent sessionConnectedEvent) {
    }
}
