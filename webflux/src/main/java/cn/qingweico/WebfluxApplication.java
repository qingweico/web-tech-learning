package cn.qingweico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author zqw
 * @date 2023-12-09
 */
@SpringBootApplication
@ServletComponentScan("cn.qingweico.controller")
public class WebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
    }

}
