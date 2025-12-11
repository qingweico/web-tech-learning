package cn.qingweico.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @author zqw
 * @date 2024/1/27
 */
@SpringBootApplication
public class RabbitmqApplication {
    // 管理界面地址 http://localhost:15672/
    public static void main(String[] args) {
        SpringApplication.run(RabbitmqApplication.class, args);
    }

}
