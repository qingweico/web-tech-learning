package cn.qingweico.rabbitmq.controller;

import cn.qingweico.rabbitmq.config.MqConstant;
import cn.qingweico.rabbitmq.config.RabbitMqConfig;
import cn.qingweico.rabbitmq.config.RabbitMqDelayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2024/1/27
 */
@Slf4j
@RestController
@RequestMapping("producer")
public class ProducerController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("send")
    public String send() {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_SMS, MqConstant.SMS_SEND_DO, "这是一条即时消息!");
        return "OK";
    }

    @GetMapping("delay")
    public String delay() {
        // ms
        long delay = 10000;
        MessagePostProcessor messagePostProcessor = (message) -> {
            message.getMessageProperties().
                    setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            message.getMessageProperties().setDelay((int) (delay));
            return message;
        };
        rabbitTemplate.convertAndSend(RabbitMqDelayConfig.EXCHANGE_DELAY,
                MqConstant.SMS_DELAY_SEND_DO,
                "延迟10s的消息",
                messagePostProcessor);
        return "OK";
    }
}
