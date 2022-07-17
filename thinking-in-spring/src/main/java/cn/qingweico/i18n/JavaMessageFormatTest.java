package cn.qingweico.i18n;

import org.junit.Test;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author zqw
 * @date 2021/12/23
 */
public class JavaMessageFormatTest {
    public static void main(String[] args) {
        int planet = 7;
        String event = "a disturbance in the Force";

        String result = MessageFormat.format(
                "At {1,time, short} on {1,date, full}, there was {2} on planet {0,number,integer}.",
                planet, new Date(), event);

        System.out.println(result);
    }

    @Test
    public void superior() {
        int planet = 7;
        String event = "a disturbance in the Force";
        String pattern = "At {1,time, short} on {1,date, full}, there was {2} on planet {0,number,integer}.";
        MessageFormat messageFormat = new MessageFormat(pattern);
        String result = messageFormat.format(new Object[]{planet, new Date(), event});
        System.out.println(result);

        // 重置消息格式 MessageFormatPattern
        messageFormat.applyPattern("This is text : {0, date, short}");
        result = messageFormat.format(new Object[]{new Date()});
        System.out.println(result);

        // 重置 Local
        messageFormat.setLocale(Locale.ENGLISH);
        messageFormat.applyPattern(pattern);
        result = messageFormat.format(new Object[]{planet, new Date(), event});
        System.out.println(result);

        // 重置 Format
        messageFormat.setFormat(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        result = messageFormat.format(new Object[]{planet, new Date(), event});
        System.out.println(result);

    }
}
