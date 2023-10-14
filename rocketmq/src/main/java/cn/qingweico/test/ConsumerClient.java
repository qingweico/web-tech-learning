package cn.qingweico.test;

import cn.qingweico.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.CountDownLatch2;
import org.springframework.util.CollectionUtils;

/**
 * @author zqw
 * @date 2023/10/14
 */
@Slf4j
public class ConsumerClient {
    public static void main(String[] args) throws Exception {
        CountDownLatch2 latch2 = new CountDownLatch2(1);
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("Consumer-Group");
        consumer.setNamesrvAddr(Constant.NAME_SRC_ADDR);
        // * 标识订阅主题中所有消息
        consumer.subscribe("OrderlyMessageTopic", "*");
        consumer.registerMessageListener((MessageListenerOrderly) (list, consumeConcurrentlyContext) -> {
            // MessageListenerConcurrently(并发模式, 默认20个线程) : 消费者会使用多个线程进行消费
            // MessageListenerOrderly(顺序模式, 使用单个线程从队列取消息)
            if (!CollectionUtils.isEmpty(list)) {
                log.info("msg list size :  {}", list.size());
                list.forEach(message -> {
                    log.info("message :  {}", new String(message.getBody()));
                });
            }
            return ConsumeOrderlyStatus.SUCCESS;
        });
        consumer.start();
        latch2.await();
    }
}
