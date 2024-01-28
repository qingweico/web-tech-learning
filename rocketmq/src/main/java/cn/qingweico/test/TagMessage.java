package cn.qingweico.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.UUID;

/**
 * 订阅关系 : 一个消费者组订阅一个 Topic 下某一个 Tag
 * 订阅关系一致 : 同一个消费者组下所有的消费者实际所订阅的 Topic 和 Tag 必须完全一致
 *
 * @author zqw
 * @date 2023/10/16
 */
@Slf4j
public class TagMessage extends AbstractMessageSend {
    @Override
    void doSendMessage(DefaultMQProducer producer) {
        try {
            // 不用的消息类型使用不同的 Topic 区分, 无法通过 Tag 区分, 若业务相关联可以使用 Tag 区分, 不同优先级的消息和不相当的消息量级, 需使用不同的 Topic
            Message m1 = new Message("TagTopic", "T1", UUID.randomUUID().toString(), "TagTopicMessage".getBytes());
            Message m2 = new Message("TagTopic", "T2", UUID.randomUUID().toString(), "TagTopicMessage".getBytes());
            producer.send(m1);
            producer.send(m2);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
