package cn.qingweico.delayTask;

import lombok.Getter;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author zqw
 * @date 2023/11/19
 */
public class DelayTask implements Delayed {
    @Getter
    final private Object data;
    final private long expire;

    public DelayTask(Object data, long expire) {
        super();
        this.data = data;
        this.expire = expire;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis(), unit);
    }

    @Override
    public int compareTo(Delayed o) {
        long delta = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return (int) delta;
    }

    @Override
    public String toString() {
        return "DelayTask{" + "data=" + data +
                ", expire=" + expire +
                '}';
    }
}
