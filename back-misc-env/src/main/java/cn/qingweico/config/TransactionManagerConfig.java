package cn.qingweico.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;

import javax.sql.DataSource;

/**
 * @author zqw
 * @date 2025/10/30
 */
@Configuration
public class TransactionManagerConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    @Bean
    public JdbcTransactionManager jdbcTransactionManager() {
        DataSource dataSource = applicationContext.getBean("dataSource", DataSource.class);
        return new JdbcTransactionManager(dataSource);
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
