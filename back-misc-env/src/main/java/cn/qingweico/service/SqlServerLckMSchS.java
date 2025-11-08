package cn.qingweico.service;

import cn.hutool.core.date.DateTime;
import cn.qingweico.config.SpringApplicationContext;
import cn.qingweico.database.SqlTmplQuery;
import cn.qingweico.supplier.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * ---------- LCK_M_SCH_S 与 LCK_M_SCH_M ----------
 * 在 SqlServer 中
 * DDL 操作需要 LCK_M_SCH_M(架构修改锁)
 * DML 操作需要 LCK_M_SCH_S(架构稳定性锁)
 * 其 LCK_M_SCH_S 与 LCK_M_SCH_S 可以同时持有,
 * 而 LCK_M_SCH_S 与 LCK_M_SCH_M 以及 LCK_M_SCH_M 与 LCK_M_SCH_M 是互斥的
 *
 * @author zqw
 * @date 2025/11/8
 */
@Slf4j
@Service
public class SqlServerLckMSchS {
    private final DataSourceTransactionManager transactionManager;
    private final SqlTmplQuery sqlTmplQuery;

    public SqlServerLckMSchS(DataSourceTransactionManager transactionManager,
                             SqlTmplQuery sqlTmplQuery) {
        this.transactionManager = transactionManager;
        this.sqlTmplQuery = sqlTmplQuery;

    }

    public void start() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("MultithreadedTxLock");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setTimeout(80);
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);
        ExecutorService threadPool = SpringApplicationContext.getBean(ExecutorService.class);
        List<String> bakTableNameList = new ArrayList<>(copyAndDeleteTable("tb_type"));
        CountDownLatch latch = new CountDownLatch(3);
        log.info("MultithreadedTxLock start...");
        try {
            CompletableFuture.runAsync(() -> {
                try {
                    delType("A");
                    insertType("A");
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    latch.countDown();
                }

            }, threadPool);

            CompletableFuture.runAsync(() -> {
                try {
                    delType("B");
                    insertType("B");
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    latch.countDown();
                }
            }, threadPool);

            CompletableFuture.runAsync(() -> {
                try {
                    delType("C");
                    insertType("C");
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    latch.countDown();
                }
            }, threadPool);
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("MultithreadedTxLock end...");
            dropBakTableList(bakTableNameList);
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
    }

    private void insertType(String type) {
        List<Object[]> params = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            params.add(new Object[]{String.valueOf(SnowflakeIdWorker.nextId()), type});
        }
        sqlTmplQuery.batchUpdate("insert into tb_type values(?,?, GETDATE())", params);
    }

    private void delType(String type) {
        sqlTmplQuery.update("delete from tb_type where type = ?", new Object[]{type});
    }


    // resolve: 事务传播行为 由PROPAGATION_REQUIRED 修改为 PROPAGATION_REQUIRES_NEW
    // PROPAGATION_REQUIRED: 如果外层有事务, 其会加入外层事务, TRUNCATE TABLE 操作
    // 带来的 LCK_M_SCH_M 锁会一直持有, 导致其他会话的 DML 操作获取不到 LCK_M_SCH_S,
    // 直到最外层的事务提交才释放锁, 造成长时间阻塞
    // PROPAGATION_REQUIRES_NEW: 会创建一个全新的独立事务, TRUNCATE 操作完成后立即提交
    // 及时释放 LCK_M_SCH_M 锁, 避免阻塞其他操作

    public List<String> copyAndDeleteTable(String... tableNameList) {
        List<String> backTableNameList = new ArrayList<>();

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("copyAndDeleteTable");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        def.setTimeout(80);
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);
        try {
            for (String tableName : tableNameList) {
                String backTableName = tableName + "_bak_" + new DateTime().toString("yyyyMMddHHmmss");

                String copyTableAndDataSql = "SELECT * INTO " + backTableName + " FROM  " + tableName;
                sqlTmplQuery.update(copyTableAndDataSql);

                String clearSql = "TRUNCATE TABLE " + tableName;
                sqlTmplQuery.update(clearSql);

                backTableNameList.add(backTableName);
            }

            transactionManager.commit(transactionStatus);
            return backTableNameList;
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            throw e;
        }
    }


    public void dropBakTableList(List<String> bakTableNameList) {
        if (!CollectionUtils.isEmpty(bakTableNameList)) {
            for (String bakTableName : bakTableNameList) {
                sqlTmplQuery.update("drop table if exists " + bakTableName);
            }
        }
    }
}
