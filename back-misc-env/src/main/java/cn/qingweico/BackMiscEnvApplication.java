package cn.qingweico;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author zqw
 * @date 2023/06/22
 */
@MapperScan(basePackages = "cn.qingweico.mapper")
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class BackMiscEnvApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackMiscEnvApplication.class, args);
    }

}
