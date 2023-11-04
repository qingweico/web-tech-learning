package cn.qingweico.test;

import org.junit.Test;
import org.springframework.core.annotation.Order;

/**
 * @author zqw
 * @date 2023/10/14
 */
public class ProducerClientTest {
    @Test
    public void syncMessage() throws InterruptedException {
        AbstractMessageSend syncMessage = new SyncMessage();
        syncMessage.sendMessage();
    }

    @Test
    public void asyncMessage() throws InterruptedException {
        AbstractMessageSend asyncMessage = new AsyncMessage();
        asyncMessage.sendMessage("AsyncMessage");
    }

    @Test
    public void onewayMessage() throws InterruptedException {
        AbstractMessageSend oneWayMessage = new OneWayMessage();
        oneWayMessage.sendMessage();
    }

    @Test
    public void delayMessage() throws InterruptedException {
        AbstractMessageSend delayMessage = new DelayMessage();
        delayMessage.sendMessage();
    }
    @Test
    public void batchMessage() throws InterruptedException {
        AbstractMessageSend batchMessage = new BatchMessage();
        batchMessage.sendMessage();
    }
    @Test
    public void orderlyMessage() throws InterruptedException {
        AbstractMessageSend orderlyMessage = new OrderlyMessage();
        orderlyMessage.sendMessage();
    }
    @Test
    public void tagMessage() throws InterruptedException {
        AbstractMessageSend tagMessage = new TagMessage();
        tagMessage.sendMessage();
    }
}
