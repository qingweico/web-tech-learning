package cn.qingweico.delayTask;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zqw
 * @date 2023/11/19
 */
@Getter
@Setter
@ToString
public class RedisBroadcastDelayTask {
    private String channel;

    private String message;


    public RedisBroadcastDelayTask(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }
}
