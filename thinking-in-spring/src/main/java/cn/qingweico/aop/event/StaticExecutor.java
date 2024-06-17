package cn.qingweico.aop.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNull;

/**
 * 传统实现事件发布 偏向静态化
 *
 * @author zqw
 * @date 2022/9/22
 * @see AopExecutor
 */
public class StaticExecutor implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    public void execute(){
        System.out.println("StaticExecutor#execute");
        applicationEventPublisher.publishEvent(new ExecutedEvent(this));
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
