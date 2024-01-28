package cn.qingweico.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author zqw
 * @date 2021/9/14
 */
@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_SMS = "exchange_sms";
    public static final String QUEUE_SMS = "queue_sms";

    @Bean(EXCHANGE_SMS)
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_SMS)
                .durable(true)
                .build();
    }

    @Bean(QUEUE_SMS)
    public Queue queue() {
        return new Queue(QUEUE_SMS);
    }

    @Bean
    public Binding binding(@Qualifier(EXCHANGE_SMS) Exchange exchange,
                           @Qualifier(QUEUE_SMS) Queue queue) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(MqConstant.SMS_ALL_DO)
                .noargs();
    }
}
