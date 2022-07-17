package cn.qingweico.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author zqw
 * @date 2022/1/25
 */
public class ApplicationEventExtension extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ApplicationEventExtension(Object source) {
        super(source);
    }

    @Override
    public Object getSource() {
        return super.getSource();
    }
}
