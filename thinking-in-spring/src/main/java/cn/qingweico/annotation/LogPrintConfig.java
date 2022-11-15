package cn.qingweico.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author zqw
 * @date 2022/9/23
 */
@Slf4j
@Component
public class LogPrintConfig {

    @Bean
    public String log() {
        log.info("log");
        return "log";
    }
}
