package cn.qingweico.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * @author zqw
 * @date 2023/11/19
 */
@RestController
@RequestMapping("/cache")
@Slf4j
public class CacheController {
    @Resource
    private ExecutorService pool;
    @GetMapping("/logThreadFormat")
    public void logThreadFormat() {
        int loop = 10;
        for(int i = 0; i < loop ;i++) {
            pool.execute(() -> {
                log.info("Nothing to do");
            });
        }
    }
}
