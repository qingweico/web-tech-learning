package cn.qingweico.config;

import cn.qingweico.database.NamedSqlTmplQuery;
import cn.qingweico.database.SqlTmplQuery;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author zqw
 * @date 2025/12/11
 */
@Configuration
@ConditionalOnClass({JdbcTemplate.class, NamedParameterJdbcTemplate.class})
public class SpringJdbcConfig {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SpringJdbcConfig(JdbcTemplate jdbcTemplate,
                            NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Bean
    public SqlTmplQuery sqlTmplQuery() {
        return new SqlTmplQuery(jdbcTemplate);
    }

    @Bean
    public NamedSqlTmplQuery namedSqlTmplQuery() {
        return new NamedSqlTmplQuery(namedParameterJdbcTemplate);
    }
}
