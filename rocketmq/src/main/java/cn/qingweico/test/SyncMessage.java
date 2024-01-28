package cn.qingweico.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * RocketMq 发送同步消息测试
 * eg : 消息发送后会有一个返回值 代表mq服务器接受到消息的一个确认 方式安全但是性能一般
 *
 * @author zqw
 * @date 2023/10/14
 */
@Slf4j
public class SyncMessage extends AbstractMessageSend {

    public void doSendMessage(DefaultMQProducer producer) {
        try {
            Message message = new Message("MsgTopic", "This is a simple sync message".getBytes());
            SendResult sendResult = producer.send(message);
            System.out.println(sendResult.getSendStatus());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
