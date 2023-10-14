package cn.qingweico.test;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * @author zqw
 * @date 2023/10/14
 */
public class AsyncMessage extends AbstractMessageSend {
    @Override
    void doSendMessage(DefaultMQProducer producer) {

        try {
            Message message = new Message("MsgTopic", "This is a simple async message".getBytes());
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("Send Success!");
                }

                @Override
                public void onException(Throwable e) {
                    System.out.printf("Send Fail!, %s", e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
