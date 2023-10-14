package cn.qingweico.test;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.Arrays;
import java.util.List;

/**
 * 批量发送消息
 *
 * @author zqw
 * @date 2023/10/14
 */
public class BatchMessage extends AbstractMessageSend {
    @Override
    void doSendMessage(DefaultMQProducer producer) {
        // 发送在同一个 topic 下的同一个队列中(topic不同会抛异常)(一个 topic 中默认4个队列)
        List<Message> messages = Arrays.asList(
                new Message("BatchMessageTopic", "This is a simple batch message".getBytes()),
                new Message("BatchMessageTopic", "This is a simple batch message".getBytes()),
                new Message("BatchMessageTopic", "This is a simple batch message".getBytes())
        );
        try {
            producer.send(messages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
