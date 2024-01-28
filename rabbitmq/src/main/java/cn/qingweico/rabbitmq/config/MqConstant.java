package cn.qingweico.rabbitmq.config;

/**
 * @author zqw
 * @date 2024/1/27
 */
public final class MqConstant {

    /**
     * rabbit mq (sms.send.d) routingKey
     */
    public static final String SMS_SEND_DO = "sms.send.do";

    /**
     * rabbit mq (sms.*.d) routingKey
     */
    public static final String SMS_ALL_DO = "sms.*.do";

    /**
     * rabbit mq (delay queue) (sms.delay.send.d) routingKey
     */
    public static final String SMS_DELAY_SEND_DO = "sms.delay.send.do";

    /**
     * rabbit mq (delay queue) (sms.delay.*.d) routingKey
     */
    public static final String SMS_DELAY_ALL_DO = "sms.delay.*.do";
}
