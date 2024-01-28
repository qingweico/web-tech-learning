package cn.qingweico.boot3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zqw
 * @date 2023/06/24
 */
@SpringBootApplication
public class Boot3EnvApplication {
    // gu install native-image 安装native支持
    // 打包项目 mvn package -Pnative -DskipTests
    // Quarkus

    public static void main(String[] args) {
        SpringApplication.run(Boot3EnvApplication.class, args);
    }

}
