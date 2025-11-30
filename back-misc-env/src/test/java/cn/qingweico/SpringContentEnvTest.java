package cn.qingweico;

import cn.qingweico.database.SqlTmplQuery;
import cn.qingweico.service.SqlServerLckMSchS;
import jodd.chalk.Chalk256;
import jodd.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

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
    @Resource
    SqlTmplQuery sqlTmplQuery;
    @Resource
    private SqlServerLckMSchS sqlServerLckMSchS;
    @Resource
    private ExecutorService pool;
    @Value("#{environment['server.port']}")
    private String serverPort;
    @Value("#{systemProperties['java.version']}")
    private String javaVersion;
    @Value("#{T(java.lang.Math).random()}")
    private String random;
    @Value("#{1 + 2}")
    private int add;
    @Value("#{1 > 2}")
    private boolean greatThan;
    @Value("#{'Hello, World!'}")
    private String literal;
    @Value("#{{'a', 'b', 'c'}}")
    private List<String> list;

    @Test
    public void spel() {
        System.out.println(serverPort);
        System.out.println(javaVersion);
        System.out.println(random);
        System.out.println(add);
        System.out.println(greatThan);
        System.out.println(literal);
        System.out.println(list);
    }

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

    @Test
    public void sqlServerLckMSchS() {
        sqlServerLckMSchS.start();
    }


    /*!!!!!!!!!!!!!!! 可重复读隔离级别下无法防止幻读的场景 (快照读和当前读混合使用)*/

    @Test
    public void displayPhantomRead() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        pool.execute(() -> {
            this.transaction1();
            latch.countDown();
        });
        ThreadUtil.sleep(3000);
        pool.execute(() -> {
            this.transaction2();
            latch.countDown();
        });
        latch.await();
    }

    @Test
    public void gapLock() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(2);
        pool.execute(() -> {
            this.transaction4();
            latch.countDown();
        });
        ThreadUtil.sleep(1000);
        pool.execute(() -> {
            this.transaction3();
            latch.countDown();
        });
        latch.await();
    }

    public void transaction1() {
        // 事务开始(指定某个区间, 先准备数据no为1、3、10三条数据)
        // 事务一 快照读
        // 事务二 插入数据
        // 事务一 快照读
        // 事务一 更新数据
        // 事务一 快照读 幻读发生
        // 事务结束
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(transactionDefinition);
        List<String> result;
        result = sqlTmplQuery.queryForList("select no from tb_goods where no > 2", "String");
        Chalk256.chalk().red().
                println("【事务一】第一次快照读................");
        System.out.println(result);
        ThreadUtil.sleep(10000);
        Chalk256.chalk().green().
                println("【事务一】等待【事务二】插入数据后第二次快照读................");
        result = sqlTmplQuery.queryForList("select no from tb_goods where no > 2", "String");
        System.out.println(result);
        Chalk256.chalk().bright(3).
                println("【事务一】更新数据................");
        int effect = sqlTmplQuery.update("update tb_goods set name = 'xxxx'  where no > 2");
        System.out.println(effect);
        result = sqlTmplQuery.queryForList("select no from tb_goods where no > 2", "String");
        Chalk256.chalk().bright(7).
                println("【事务一】第三次快照读................");
        System.out.println(result);
        ThreadUtil.sleep(30000);
        Chalk256.chalk().bright(5).
                println("【事务一】提交................");
        dataSourceTransactionManager.commit(transaction);

    }


    public void transaction2() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(transactionDefinition);
        sqlTmplQuery.update("insert into tb_goods (no, name)" +
                "values ('6', 6)");
        Chalk256.chalk().bright(4).
                println("【事务二】插入数据................");
        dataSourceTransactionManager.commit(transaction);
        Chalk256.chalk().bright(6).
                println("【事务二】事务已提交................");
    }

    public void transaction3() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(transactionDefinition);
        Chalk256.chalk().bright(4).
                println("【事务三】插入数据................");
        sqlTmplQuery.update("insert into tb_goods (no, name)" +
                "values ('7', 7)");
        dataSourceTransactionManager.commit(transaction);
        Chalk256.chalk().bright(6).
                println("【事务三】事务已提交................");
    }

    public void transaction4() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(transactionDefinition);
        List<String> result;
        Chalk256.chalk().green().
                println("【事务四】第一次当前读................");
        result = sqlTmplQuery.queryForList("select no from tb_goods where no > 2 for update", "String");
        System.out.println(result);
        Chalk256.chalk().bright(5).
                println("【等待三秒】................");
        ThreadUtil.sleep(3000);
        Chalk256.chalk().cyan().
                println("【事务四】第二次当前读................");
        result = sqlTmplQuery.queryForList("select no from tb_goods where no > 2 for update", "String");
        System.out.println(result);
        Chalk256.chalk().red().
                println("【事务四】提交................");
        dataSourceTransactionManager.commit(transaction);
        Chalk256.chalk().bright(5).
                println("【等待三秒】................");
        ThreadUtil.sleep(3000);
        Chalk256.chalk().yellow().
                println("【事务四】事务提交后读取................");
        result = sqlTmplQuery.queryForList("select no from tb_goods where no > 2", "String");
        System.out.println(result);
    }
}
