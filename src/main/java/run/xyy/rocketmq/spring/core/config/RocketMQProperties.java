package run.xyy.rocketmq.spring.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author XYY
 * @since 1.0
 */
@ConfigurationProperties(ConfigConstants.CONFIG_PREFIX)
@Data
public class RocketMQProperties {
    /**
     * 代理的地址
     */
    private String proxyUrl;
    private String accessKey = "XYYRocketMQ";
    private String secretKey = "dZqSiYGOp33vvHO7ILlHLft7PjlHKuS";
    /**
     * 预绑定的主题
     */
    private String[] topics;
}
