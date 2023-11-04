package cn.qingweico.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zqw
 * @date 2023/10/27
 */
@RestController
public class PortController {

    @Value("${server.port}")
    private String port;
    @GetMapping("port")
    public String getPort() {
        return port;
    }
}
