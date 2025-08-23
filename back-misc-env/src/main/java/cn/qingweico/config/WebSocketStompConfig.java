package cn.qingweico.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author zqw
 * @date 2025/8/20
 */
@Configuration
// 开启 Spring 的 WebSocket 消息代理功能
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 配置消息代理(Message Broker)
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 客户端可以订阅这些前缀的目的地, 以 /topic 开头的消息都会被发送到消息代理
        config.enableSimpleBroker("/topic");
        // 配置一个或多个前缀, 过滤需要被注解方法处理的消息
        // 以 /app 开头的消息会被路由到带有 @MessageMapping 注解的方法中
        config.setApplicationDestinationPrefixes("/app");
    }
    /**
     * 注册 STOMP 端点(Endpoint)
     * 客户端将通过这个 URL 来建立 WebSocket 连接
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
