package cn.qingweico;

import cn.qingweico.datetime.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author zqw
 * @date 2025/9/1
 * @see ResourceBundleMessageSource
 * @see MessageSourceProperties
 * @see AbstractMessageSource
 */
@SpringBootTest
public class MessageSourceTest {

    @Autowired
    private MessageSource messageSource;

    @Test
    public void messageSource() {
        System.out.println(messageSource.getMessage("welcome.message", new Object[]{System.getProperty("user.name"), DateUtil.now()}, Locale.getDefault()));
    }
}
