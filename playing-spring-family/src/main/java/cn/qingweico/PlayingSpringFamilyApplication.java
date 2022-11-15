package cn.qingweico;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zqw
 * @date 2022/8/20
 */
@Slf4j
@SpringBootApplication
public class PlayingSpringFamilyApplication implements CommandLineRunner {
    @Resource
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(PlayingSpringFamilyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        showConnection();
    }

    public void showConnection() throws SQLException {

        // Actuator 所支持的连接: http://localhost:8080/actuator/
        // http://localhost:8080/actuator/beans

        // H2 数据库
        log.info("dataSource: {}", dataSource.toString());
        Connection connection = dataSource.getConnection();
        log.info("connection: {}", connection.toString());
        connection.close();
    }
}
