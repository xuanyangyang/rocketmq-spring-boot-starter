package run.xyy.rocketmq.spring.core.producer;

import org.apache.rocketmq.client.apis.*;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.ProducerBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import run.xyy.rocketmq.spring.core.config.RocketMQProperties;

/**
 * @author XYY
 * @since 1.0
 */
public class ProducerFactory implements FactoryBean<Producer> {
    @Autowired
    private RocketMQProperties rocketMQProperties;

    @Override
    public Producer getObject() throws Exception {
        String accessKey = rocketMQProperties.getAccessKey();
        String secretKey = rocketMQProperties.getSecretKey();
        ClientConfigurationBuilder clientConfigurationBuilder = ClientConfiguration.newBuilder()
                .setEndpoints(rocketMQProperties.getProxyUrl());
        if (accessKey != null && secretKey != null) {
            SessionCredentialsProvider sessionCredentialsProvider = new StaticSessionCredentialsProvider(accessKey.strip(), secretKey.strip());
            clientConfigurationBuilder.setCredentialProvider(sessionCredentialsProvider);
        }
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        ProducerBuilder producerBuilder = provider.newProducerBuilder();
        // 初始化Producer时需要设置通信配置以及预绑定的Topic。
        String[] topics = rocketMQProperties.getTopics();
        if (topics != null) {
            producerBuilder.setTopics(topics);
        }
        producerBuilder.setClientConfiguration(clientConfigurationBuilder.build());
        return producerBuilder.build();
    }

    @Override
    public Class<?> getObjectType() {
        return Producer.class;
    }
}
