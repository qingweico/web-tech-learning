package cn.qingweico.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zqw
 * @date 2025/8/9
 */
@Data
@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {
    private String mode = "single";
    private String host = "localhost";
    private int port = 6379;
    private String password;
    private int database = 0;
    private int timeout = 3000;
    private int poolSize = 64;
    private int minIdleSize = 24;

    // 集群模式配置
    private String clusterNodes;
    private int scanInterval = 1000;
    private String readMode = "SLAVE";
    private String subscriptionMode = "SLAVE";

    // 哨兵模式配置
    private String masterName;
    private String sentinelAddresses;

    // 主从模式配置
    private String masterAddress;
    private String slaveAddresses;
}
