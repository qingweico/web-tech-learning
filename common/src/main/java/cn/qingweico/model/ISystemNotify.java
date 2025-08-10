package cn.qingweico.model;

/**
 * @author zqw
 * @date 2025/8/2
 */
public interface ISystemNotify {
    /**
     * 推送系统重要通知
     * 该接口实现不应抛出异常和阻塞应用,尽量采用异步方式
     *
     * @param type    消息类型/分组
     * @param message 内容
     */
    void publish(String type, String message);

}
