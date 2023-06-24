package cn.qingweico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author zqw
 */
@MapperScan(basePackages = {"cn.qingweico.mapper"})
@SpringBootApplication
public class TkMapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(TkMapperApplication.class, args);
    }

}
