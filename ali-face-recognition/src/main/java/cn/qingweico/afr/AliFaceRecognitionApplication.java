package cn.qingweico.afr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * readme: 使用 阿里人脸比对 + Mongodb Grid FS 实现人脸比对
 *
 * @author zqw
 * @date 2022/3/5
 */
@SpringBootApplication(scanBasePackages = {"cn.qingweico"})
@MapperScan(basePackages = "cn.qingweico.afr.mapper")
public class AliFaceRecognitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(AliFaceRecognitionApplication.class, args);
    }

}
