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
        producer.setVipChannelEnabled(false);
        try {
            producer.start();
            // 如果 nameserver 返回的 broker 地址是容器名称(例如rocketmq-broker) 外部客户端无法解析
            // 解决方案:
            // 1. 配置 hosts 文件 127.0.0.1 rocketmq-broker(broker的镜像名称)
            // 2. 或者修改 brokerIP1 为宿主机 IP(但这会导致 Dashboard 无法访问,如下提示)
            // org.apache.rocketmq.remoting.exception.RemotingConnectException: connect to xxx:10911 failed
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
