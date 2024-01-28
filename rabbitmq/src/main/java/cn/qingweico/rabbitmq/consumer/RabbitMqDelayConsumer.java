package cn.qingweico.rabbitmq.consumer;

import cn.qingweico.rabbitmq.config.RabbitMqDelayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author zqw
 * @date 2021/9/15
 */
@Slf4j
@Component
public class RabbitMqDelayConsumer {


    @RabbitListener(queues = {RabbitMqDelayConfig.QUEUE_DELAY})
    public void listenQueue(String payLoad, Message message) {
        log.info("payLoad ---> {}", payLoad);
        log.info("message ---> {}", new String(message.getBody()));
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("routingKey ---> {}", routingKey);
    }
}
