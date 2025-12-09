package cn.qingweico.kafka.service;

import cn.qingweico.kafka.dto.OrderEvent;
import cn.qingweico.kafka.dto.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 *
 * Kafka 生产者服务
 *
 * @author zqw
 * @date 2025/12/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topic.user-topic}")
    private String userTopic;

    @Value("${spring.kafka.topic.order-topic}")
    private String orderTopic;

    @Value("${spring.kafka.topic.notification-topic}")
    private String notificationTopic;

    /**
     * 发送用户事件
     */
    public void sendUserEvent(UserEvent userEvent) {
        String key = userEvent.getUserId();
        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(userTopic, key, userEvent).completable();

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("发送用户事件成功: topic={}, key={}, partition={}, offset={}",
                        userTopic, key,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("发送用户事件失败: {}", ex.getMessage());
                // 这里可以添加重试逻辑
            }
        });
    }

    /**
     * 发送订单事件
     */
    public void sendOrderEvent(OrderEvent orderEvent) {
        String key = orderEvent.getOrderId();
        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(orderTopic, key, orderEvent).completable();

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("发送订单事件成功: topic={}, key={}, partition={}, offset={}",
                        orderTopic, key,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("发送订单事件失败: {}", ex.getMessage());
            }
        });
    }

    /**
     * 发送通用消息
     */
    public void sendMessage(String topic, String key, Object message) {
        kafkaTemplate.send(topic, key, message).completable()
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("消息发送成功: topic={}, key={}", topic, key);
                    } else {
                        log.error("消息发送失败: topic={}, error={}", topic, ex.getMessage());
                    }
                });
    }

    /**
     * 发送通知
     */
    public void sendNotification(String message) {
        kafkaTemplate.send(notificationTopic, "notification", message);
    }
}
