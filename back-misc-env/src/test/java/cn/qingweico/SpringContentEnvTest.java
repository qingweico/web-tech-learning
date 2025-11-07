package cn.qingweico;

import cn.qingweico.database.SqlTmplQuery;
import jodd.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2025/10/30
 */
@Slf4j
@SpringBootTest(classes = BackMiscEnvApplication.class)
public class SpringContentEnvTest {
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    private JdbcTransactionManager jdbcTransactionManager;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Resource
    SqlTmplQuery sqlTmplQuery;
    @Test
    public void programmaticTx() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            // something
            dataSourceTransactionManager.commit(transaction);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            dataSourceTransactionManager.rollback(transaction);
        }

    }

    @Test
    public void transactionTemplate() {
        transactionTemplate.execute(status -> null);
    }

    @Test
    public void redisTemplate() {
        redissonClient.getBucket("ttl").set(System.currentTimeMillis());
        redisTemplate.execute(new SessionCallback<>() {
            @NotNull
            @Override
            public Object execute(@NotNull RedisOperations redisOperations) throws DataAccessException {
                // 原始值
                System.out.println(redisOperations.opsForValue().get("ttl"));
                redisOperations.multi();
                // null 事务开始后和事务提交前都看不到事务的中间结果
                // 在 multi 和 exec 之间, 任何对 Redis 的读取操作都不会看到事务的中间结果
                System.out.println(redisOperations.opsForValue().get("ttl"));
                ThreadUtil.sleep(3000);
                // 取消事务, 清空所有排队的命令
                // redisOperations.discard();
                // 只有在调用 exec 时, 事务中的所有命令才会按顺序执行
                return redisOperations.exec();
            }
        });
    }

    @Test
    public void savepoint() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        TransactionStatus transaction = jdbcTransactionManager.getTransaction(transactionDefinition);
        sqlTmplQuery.update("INSERT INTO `tb_goods`(id, name, no) " +
                "VALUES (1, 'Product 1', 'P001');");
        Object savepoint = transaction.createSavepoint();
        sqlTmplQuery.update("INSERT INTO `tb_goods`(id, name, no) " +
                "VALUES (2, 'Product 2', 'P002');");
        // 回滚到指定 savepoint 之前的状态
        transaction.rollbackToSavepoint(savepoint);
        transaction.releaseSavepoint(savepoint);
        jdbcTransactionManager.commit(transaction);
    }

}
