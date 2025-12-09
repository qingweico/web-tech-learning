package cn.qingweico.kafka.controller;

import cn.qingweico.kafka.dto.OrderEvent;
import cn.qingweico.kafka.dto.UserEvent;
import cn.qingweico.kafka.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author zqw
 * @date 2025/12/4
 */
@RestController
@RequestMapping("/api/kafka")
@RequiredArgsConstructor
public class KafkaController {
    private final KafkaProducerService kafkaProducerService;

    /**
     * 发送用户事件
     */
    @PostMapping("/user-event")
    public ResponseEntity<String> sendUserEvent(@RequestParam String username, @RequestParam UserEvent.UserAction action) {

        UserEvent userEvent = new UserEvent(UUID.randomUUID().toString(), username, action, "User action: " + action, LocalDateTime.now());

        kafkaProducerService.sendUserEvent(userEvent);
        return ResponseEntity.ok("用户事件已发送: " + username);
    }

    /**
     * 发送订单事件
     */
    @PostMapping("/order-event")
    public ResponseEntity<String> sendOrderEvent(@RequestParam String userId, @RequestParam String productId, @RequestParam Integer quantity, @RequestParam BigDecimal amount, @RequestParam OrderEvent.OrderStatus status) {

        OrderEvent orderEvent = new OrderEvent(UUID.randomUUID().toString(), userId, productId, quantity, amount, status, LocalDateTime.now());

        kafkaProducerService.sendOrderEvent(orderEvent);
        return ResponseEntity.ok("订单事件已发送: " + orderEvent.getOrderId());
    }

    /**
     * 发送自定义消息
     */
    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(@RequestParam String topic, @RequestParam String key, @RequestParam String message) {

        kafkaProducerService.sendMessage(topic, key, message);
        return ResponseEntity.ok("消息已发送到主题: " + topic);
    }

    /**
     * 发送通知
     */
    @PostMapping("/send-notification")
    public ResponseEntity<String> sendNotification(@RequestParam String message) {
        kafkaProducerService.sendNotification(message);
        return ResponseEntity.ok("通知已发送");
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Kafka Producer is healthy");
    }

    /**
     * 批量发送测试数据
     */
    @PostMapping("/generate-test-data")
    public ResponseEntity<String> generateTestData(@RequestParam(defaultValue = "10") int count) {

        for (int i = 0; i < count; i++) {
            // 生成用户事件
            UserEvent.UserAction[] userActions = UserEvent.UserAction.values();
            UserEvent userEvent = new UserEvent("user-" + i, "user" + i + "@example.com", userActions[i % userActions.length], "Test user event " + i, LocalDateTime.now());
            kafkaProducerService.sendUserEvent(userEvent);

            // 生成订单事件
            OrderEvent.OrderStatus[] orderStatuses = OrderEvent.OrderStatus.values();
            OrderEvent orderEvent = new OrderEvent("order-" + i, "user-" + (i % 5), "product-" + (i % 3), (i % 5) + 1, BigDecimal.valueOf((i + 1) * 100L), orderStatuses[i % orderStatuses.length], LocalDateTime.now());
            kafkaProducerService.sendOrderEvent(orderEvent);

            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
        }

        return ResponseEntity.ok("已生成 " + count + " 条测试数据");
    }
}
