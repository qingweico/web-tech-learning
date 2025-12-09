package cn.qingweico.kafka.service;

import cn.qingweico.kafka.dto.OrderEvent;
import cn.qingweico.kafka.dto.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * Kafka 消费者服务
 *
 * @author zqw
 * @date 2025/12/4
 */
@Slf4j
@Service
public class KafkaConsumerService {
    /**
     * 消费用户事件
     */
    @KafkaListener(topics = "${spring.kafka.topic.user-topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeUserEvent(@Payload UserEvent userEvent, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition, @Header(KafkaHeaders.OFFSET) long offset, Acknowledgment ack) {

        log.info("收到用户事件: key={}, partition={}, offset={}, event={}", key, partition, offset, userEvent);

        try {
            // 处理业务逻辑
            processUserEvent(userEvent);

            // 手动提交 offset
            ack.acknowledge();
            log.info("用户事件处理完成: userId={}", userEvent.getUserId());

        } catch (Exception e) {
            log.error("处理用户事件失败: {}", e.getMessage());
            // 可以根据异常类型决定是否重试
        }
    }

    /**
     * 消费订单事件
     */
    @KafkaListener(topics = "${spring.kafka.topic.order-topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrderEvent(@Payload OrderEvent orderEvent, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition, @Header(KafkaHeaders.OFFSET) long offset, Acknowledgment ack) {

        log.info("收到订单事件: key={}, partition={}, offset={}, event={}", key, partition, offset, orderEvent);

        try {
            // 处理订单逻辑
            processOrderEvent(orderEvent);

            // 手动提交 offset
            ack.acknowledge();
            log.info("订单事件处理完成: orderId={}", orderEvent.getOrderId());

        } catch (Exception e) {
            log.error("处理订单事件失败: {}", e.getMessage());
        }
    }

    /**
     * 消费通知消息
     */
    @KafkaListener(topics = "${spring.kafka.topic.notification-topic}", groupId = "notification-group")
    public void consumeNotification(String message) {
        log.info("收到通知: {}", message);
        // 发送邮件、短信、推送等
        sendNotification(message);
    }

    private void processUserEvent(UserEvent userEvent) {
        // 模拟业务处理
        switch (userEvent.getAction()) {
            case REGISTER:
                log.info("新用户注册: {}", userEvent.getUsername());
                // 发送欢迎邮件
                break;
            case LOGIN:
                log.info("用户登录: {}", userEvent.getUsername());
                // 更新最后登录时间
                break;
            case UPDATE_PROFILE:
                log.info("用户更新资料: {}", userEvent.getUsername());
                // 同步到其他系统
                break;
        }
        // 模拟处理耗时
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
    }

    private void processOrderEvent(OrderEvent orderEvent) {
        // 模拟业务处理
        switch (orderEvent.getStatus()) {
            case CREATED:
                log.info("新订单创建: orderId={}, amount={}", orderEvent.getOrderId(), orderEvent.getAmount());
                // 扣减库存
                break;
            case PAID:
                log.info("订单已支付: orderId={}", orderEvent.getOrderId());
                // 安排发货
                break;
            case SHIPPED:
                log.info("订单已发货: orderId={}", orderEvent.getOrderId());
                // 发送物流通知
                break;
        }
        // 模拟处理耗时
        try {
            Thread.sleep(150);
        } catch (InterruptedException ignored) {
        }
    }

    private void sendNotification(String message) {
        log.info("发送通知: {}", message);
        // 实际项目中调用通知服务
    }
}
