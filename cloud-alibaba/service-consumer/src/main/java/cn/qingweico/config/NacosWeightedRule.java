package cn.qingweico.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 负载均衡 基于权重的算法
 * @author zqw
 * @date 2023/10/27
 */
@Configuration
@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {

    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    @Override
    public Server choose(Object key) {
        ILoadBalancer iLoadBalancer = this.getLoadBalancer();
        BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) iLoadBalancer;
        String name = baseLoadBalancer.getName();
        NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
        try {
            Instance instance = namingService.selectOneHealthyInstance(name);
            log.info("负载均衡, 选择的实例是 {}, 端口号是 {}", instance, instance.getPort());
            return new NacosServer(instance);
        } catch (NacosException e) {
            log.error("NacosException : {}", e.getMessage());
            return null;
        }
    }
}
