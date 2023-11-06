package cn.qinwweico.util.pool;



import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zqw
 * @date 2023/4/23
 */
@Slf4j
public class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // main thread execute
        if (!executor.isShutdown()) {
            r.run();
        }
        // just tip
        log.info("Thread Pool Rejected Task, Main Thread Execute...");
    }
}
