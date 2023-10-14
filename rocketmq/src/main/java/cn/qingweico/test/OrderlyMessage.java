package cn.qingweico.test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.Arrays;
import java.util.List;

/**
 * 顺序消息
 *
 * @author zqw
 * @date 2023/10/14
 */
public class OrderlyMessage extends AbstractMessageSend {

    private final List<PaymentModel> paymentModels = Arrays.asList(
            new PaymentModel(null, 1L, "0", "用户下单"),
            new PaymentModel(1L, 1L, "1", "用户支付"),
            new PaymentModel(1L, 1L, "1", "提示发货"),
            new PaymentModel(null, 2L, "0", "用户下单"),
            new PaymentModel(2L, 2L, "1", "用户支付"),
            new PaymentModel(2L, 2L, "1", "提示发货")
    );

    @Override
    void doSendMessage(DefaultMQProducer producer) {
        paymentModels.forEach(paymentModel -> {
            Message message = new Message("OrderlyMessageTopic", paymentModel.toString().getBytes());
            try {
                producer.send(message, (mqs, msg, arg) -> {
                    // 用户ID相同的发送至同一个队列
                    int hashcode = arg.toString().hashCode();
                    return mqs.get(hashcode % mqs.size());
                }, paymentModel.getUserId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}

@AllArgsConstructor
@ToString
@Getter
class PaymentModel {
    Long orderNo;
    Long userId;
    String payStatus;
    String desc;
}
