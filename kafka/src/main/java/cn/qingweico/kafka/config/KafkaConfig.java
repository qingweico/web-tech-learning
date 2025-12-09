package cn.qingweico.kafka.config;

import cn.qingweico.kafka.dto.OrderEvent;
import cn.qingweico.kafka.dto.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zqw
 * @date 2025/12/4
 */
@Slf4j
@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.topic.user-topic}")
    private String userTopic;

    @Value("${spring.kafka.topic.order-topic}")
    private String orderTopic;

    @Value("${spring.kafka.topic.notification-topic}")
    private String notificationTopic;

    @Value("${spring.kafka.topic.partitions:3}")
    private Integer partitions;

    @Value("${spring.kafka.topic.replicas:1}")
    private Short replicas;

    /**
     * 自动创建主题
     */
    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name(userTopic).partitions(partitions).replicas(replicas).build();
    }

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder.name(orderTopic).partitions(partitions).replicas(replicas).build();
    }

    @Bean
    public NewTopic notificationTopic() {
        return TopicBuilder.name(notificationTopic).partitions(1).replicas(replicas).build();
    }

    /**
     * JSON 消息转换器
     */
    @Bean
    public RecordMessageConverter jsonMessageConverter() {
        StringJsonMessageConverter converter = new StringJsonMessageConverter();
        // 配置类型映射
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> typeMappings = new HashMap<>();
        typeMappings.put("orderEvent", OrderEvent.class);
        typeMappings.put("userEvent", UserEvent.class);
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        typeMapper.setIdClassMapping(typeMappings);
        converter.setTypeMapper(typeMapper);
        return converter;
    }

    /**
     * Kafka Template 配置
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory, RecordMessageConverter messageConverter) {
        KafkaTemplate<String, Object> template = new KafkaTemplate<>(producerFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    /**
     * 消费者容器工厂配置
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(ConsumerFactory<String, Object> consumerFactory, RecordMessageConverter messageConverter) {

        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setMessageConverter(messageConverter);

        // 配置并发消费者
        factory.setConcurrency(partitions);

        // 手动提交 offset
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        // 可以设置批量消费
        // factory.setBatchListener(true);

        // 设置错误处理器
        factory.setCommonErrorHandler(loggingErrorHandler());

        return factory;
    }

    @Bean
    public CommonErrorHandler loggingErrorHandler() {
        return new DefaultErrorHandler((record, exception) -> {
            log.error("消息处理失败, 发送到死信队列: topic={}, partition={}, offset={}", record.topic(), record.partition(), record.offset());
            log.error("异常信息: {}", exception.getMessage());
            // 重试2次间隔一秒
        }, new FixedBackOff(1000L, 2L));
    }
}
