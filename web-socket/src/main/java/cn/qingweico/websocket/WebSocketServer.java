package cn.qingweico.websocket;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zqw
 * @date 2022/8/22
 */
@ServerEndpoint("/websocket/{clientId}")
@Component
@Slf4j
public class WebSocketServer {

    /**
     * 静态变量 用来记录当前在线连接数
     */
    private static int onlineCount = 0;
    /**
     * 存放每个客户端对应的WebSocket对象
     */
    private static final ConcurrentHashMap<String, WebSocketServer> WEB_SOCKET_MAP = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话 需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收clientId
     */
    private String clientId = "";


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("clientId") String clientId) {
        this.session = session;
        this.clientId = clientId;
        if (WEB_SOCKET_MAP.containsKey(clientId)) {
            WEB_SOCKET_MAP.remove(clientId);
            WEB_SOCKET_MAP.put(clientId, this);
        } else {
            WEB_SOCKET_MAP.put(clientId, this);
            addOnlineCount();
        }

        log.info("客户端 {} 连接, 当前在线客户端为: {}", clientId, getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("客户端: {}, 网络异常", clientId);
        }
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("clientId") String clientId) {
        if (WEB_SOCKET_MAP.containsKey(clientId)) {
            WEB_SOCKET_MAP.remove(clientId);
            // 从set中删除
            subOnlineCount();
        }
        log.info("客户端 {} 退出, 当前在线客户端为: {}", clientId, getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("客户端 {} 消息, 报文: {}", clientId, message);
        //可以群发消息
        //消息保存到数据库、redis
        if (StrUtil.isNotBlank(message)) {
            try {
                //解析发送的报文
                Map<String, Boolean> json = new HashMap<>(8);
                json.put("isConnect", true);
                String str = JSON.toJSONString(json);
                sendInfo(str, clientId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param session Session
     * @param error   Throwable
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("request uri: {}", session.getRequestURI());
        log.error("客户端 {} 错误, 原因: {}", this.clientId, error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void sendMessageAll(String message) {
        for (WebSocketServer item : WEB_SOCKET_MAP.values()) {
            log.info("客户消息:" + item.clientId + ",报文:" + message);
            item.session.getAsyncRemote().sendText(message);
        }
    }


    /**
     * 给客户端clientId,发送消息
     */
    public void sendInfo(String message, @PathParam("clientId") String clientId) throws IOException {
        log.info("发送消息到: {}, 报文: {}", clientId, message);
        if (StrUtil.isNotBlank(clientId) && WEB_SOCKET_MAP.containsKey(clientId)) {
            WEB_SOCKET_MAP.get(clientId).sendMessage(message);
        } else {
            log.error("客户端 {}, 不在线!", clientId);
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
