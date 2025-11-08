package cn.qingweico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;

import javax.sql.DataSource;

/**
 * @author zqw
 * @date 2025/10/30
 */
@Configuration
public class TransactionManagerConfig {

    @Bean
    public JdbcTransactionManager jdbcTransactionManager() {
        DataSource dataSource = SpringApplicationContext.getBean(DataSource.class);
        return new JdbcTransactionManager(dataSource);
    }
}
