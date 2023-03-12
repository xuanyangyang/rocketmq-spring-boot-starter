package run.xyy.rocketmq.spring.core.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import run.xyy.rocketmq.spring.core.producer.ProducerFactory;
import run.xyy.rocketmq.spring.core.service.ConsumerService;

/**
 * @author XYY
 * @since 1.0
 */
@AutoConfiguration
@EnableConfigurationProperties(RocketMQProperties.class)
public class RocketAutoConfig {
    @Bean
    public ProducerFactory producerFactory() {
        return new ProducerFactory();
    }

    @Bean
    public ConsumerService consumerService() {
        return new ConsumerService();
    }
}
