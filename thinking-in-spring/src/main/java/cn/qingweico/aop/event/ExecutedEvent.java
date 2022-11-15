package cn.qingweico.aop.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author zqw
 * @date 2022/9/22
 */
public class ExecutedEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ExecutedEvent(Object source) {
        super(source);
    }
}
