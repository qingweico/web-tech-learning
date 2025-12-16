package cn.qingweico.config;

import cn.qingweico.model.ISystemNotify;
import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zqw
 * @date 2025/8/2
 */
public abstract class AbstractAsyncEventNotify implements ISystemNotify {
    Logger logger = LoggerFactory.getLogger(AbstractAsyncEventNotify.class);

    private static class NotifyInfo {
        private String type;
        private String message;
    }

    private boolean shutdown;
    private final ExecutorService executorService;
    private final Disruptor<NotifyInfo> disruptor;
    private final EventTranslatorVararg<NotifyInfo> translator;

    public AbstractAsyncEventNotify() {
        executorService = Executors.newSingleThreadExecutor();

        translator = (event, sequence, args) -> {
            event.type = (String) args[0];
            event.message = ((String) args[1]);
        };
        disruptor = new Disruptor<>(
                NotifyInfo::new,
                // 设置 RingBuffer 大小
                1024 * 4,
                // 设置线程工厂(ExecutorService)
                Executors.defaultThreadFactory(),
                // 设置生产者类型(MULTI/SINGLE)
                ProducerType.MULTI,
                // 设置等待策略
                new SleepingWaitStrategy()
        );
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            try {
                publishAsync(event.type, event.message);
            } catch (Exception e) {
                logger.warn("通知[{}-{}]发送失败", event.type, event.message, e);
            }
        });

        disruptor.start();
    }

    @Override
    public final void publish(String type, String message) {
        if (!shutdown) {
            RingBuffer<NotifyInfo> ringBuffer = disruptor.getRingBuffer();

            ringBuffer.tryPublishEvent(translator, type, message);
        }
    }

    /**
     * 推送系统重要通知
     * 该接口必定异步单线程执行
     *
     * @param type    消息类型/分组
     * @param message 内容
     */
    abstract void publishAsync(String type, String message) throws Exception;

    @PreDestroy
    public void shutdown() {
        shutdown = true;
        disruptor.shutdown();
        executorService.shutdownNow();
    }
}
