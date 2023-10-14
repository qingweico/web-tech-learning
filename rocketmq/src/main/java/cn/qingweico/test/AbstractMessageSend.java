package cn.qingweico.test;

import cn.qingweico.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.CountDownLatch2;

/**
 * @author zqw
 * @date 2023/10/14
 */
@Slf4j
public abstract class AbstractMessageSend {

    /**
     * sendMessage
     *
     * @param producerGroup the producer group
     * @return DefaultMQProducer
     */
    final DefaultMQProducer init(final String producerGroup) {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(Constant.NAME_SRC_ADDR);
        try {
            producer.start();
        } catch (MQClientException e) {
            log.error("MQClientException : {}", e.getMessage());
        }
        return producer;
    }

    final void shutdown(final DefaultMQProducer producer) {
        producer.shutdown();
    }


    final void sendMessage(final String producerGroup) throws InterruptedException {
        CountDownLatch2 latch2 = new CountDownLatch2(1);
        DefaultMQProducer producer = init(producerGroup);
        doSendMessage(producer);
        log.info("[client : {}], send to {} complete!", producer.getClientIP(), producer.getNamesrvAddr());
        latch2.await();
        shutdown(producer);
    }

    final void sendMessage() throws InterruptedException {
        sendMessage("Producer-Group");
    }

    /**
     * doSendMessage
     *
     * @param producer DefaultMQProducer
     */
    abstract void doSendMessage(DefaultMQProducer producer);
}
