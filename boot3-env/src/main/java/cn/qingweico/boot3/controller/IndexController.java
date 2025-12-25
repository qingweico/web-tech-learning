package cn.qingweico.boot3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zqw
 * @date 2025/12/25
 */
@RestController
@RequestMapping("index")
public class IndexController {

    @GetMapping("native")
    public String index() {
        return "Native Image";
    }
}
