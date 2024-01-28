package cn.qingweico.rabbitmq.consumer;

import cn.qingweico.rabbitmq.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zqw
 * @date 2024/1/27
 */
@Slf4j
@Component
public class RabbitMqConsumer {

    @RabbitListener(queues = {RabbitMqConfig.QUEUE_SMS})
    public void listenQueue(String payLoad, Message message) {
        log.info("payLoad ---> {}", payLoad);
        log.info("message ---> {}", new String(message.getBody()));
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("routingKey ---> {}", routingKey);
    }
}
