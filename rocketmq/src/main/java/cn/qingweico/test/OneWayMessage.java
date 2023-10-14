package cn.qingweico.test;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * 单向消息 : 不关心发送结果, 吞吐量大, 存在丢失消息的风险, 主要用于发送日志
 * @author zqw
 * @date 2023/10/14
 */
public class OneWayMessage extends AbstractMessageSend {
    @Override
    void doSendMessage(DefaultMQProducer producer) {
        Message message = new Message("OnewayTopic", "This is a simple oneway message".getBytes());
        try {
            producer.sendOneway(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
