package cn.qingweico;

import cn.qingweico.service.ContentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author zqw
 * @date 2020/11/8
 */
@SpringBootApplication
public class Starter implements CommandLineRunner {

    @Resource
    ContentService contentService;

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(contentService.hasSucceedParseContent("C++"));
    }
}
