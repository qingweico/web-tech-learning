package cn.qingweico.test;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 *
 * 延迟消息
 *
 * @author zqw
 * @date 2023/10/14
 */
public class DelayMessage extends AbstractMessageSend{
    @Override
    void doSendMessage(DefaultMQProducer producer) {
        try {
            Message message = new Message("DelayMessageTopic", "This is a simple delay message".getBytes());
            message.setDelayTimeLevel(3);
            producer.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
