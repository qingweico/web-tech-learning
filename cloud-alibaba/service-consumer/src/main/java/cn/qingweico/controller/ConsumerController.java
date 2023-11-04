package cn.qingweico.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zqw
 * @date 2023/10/27
 */
@RestController
@Slf4j
public class ConsumerController {

    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/getServiceInstances")
    public List<ServiceInstance> getServiceInstances() {
        return discoveryClient.getInstances("service-provider");
    }

    @GetMapping("getPort")
    public String port() {
        String url = "http://service-provider/port";
        return restTemplate.getForObject(url, String.class);
    }
}
