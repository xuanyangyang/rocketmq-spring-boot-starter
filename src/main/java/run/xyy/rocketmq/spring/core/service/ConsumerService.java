package run.xyy.rocketmq.spring.core.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.*;
import org.apache.rocketmq.client.apis.consumer.*;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import run.xyy.rocketmq.spring.core.config.RocketMQProperties;
import run.xyy.rocketmq.spring.core.consumer.Consumer;
import run.xyy.rocketmq.spring.core.consumer.MessageContext;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * @author XYY
 * @since 1.0
 */
@Slf4j
public class ConsumerService implements InitializingBean {
    @Autowired
    private RocketMQProperties rocketMQProperties;
    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, PushConsumer> rocketMQConsumerMap = Collections.emptyMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("rocketMQProperties:{}", rocketMQProperties);
        init();
    }

    private List<Consumer> getConsumerList() {
        Map<String, Consumer> consumerMap = applicationContext.getBeansOfType(Consumer.class);
        return new ArrayList<>(consumerMap.values());
    }

    private Map<String, List<Consumer>> getConsumerGroupByConsumerGroup() {
        List<Consumer> consumerList = getConsumerList();
        log.info("扫描到{}个消费者", consumerList.size());
        Map<String, List<Consumer>> consumerMap = new HashMap<>();
        for (Consumer consumer : consumerList) {
            consumerMap.computeIfAbsent(consumer.getConsumerGroup(), consumerGroup -> new ArrayList<>()).add(consumer);
        }
        return consumerMap;
    }

    private void init() {
        log.info("开始初始化消费者");
        String accessKey = rocketMQProperties.getAccessKey();
        String secretKey = rocketMQProperties.getSecretKey();
        ClientConfigurationBuilder clientConfigurationBuilder = ClientConfiguration.newBuilder()
                .setEndpoints(rocketMQProperties.getProxyUrl());
        if (accessKey != null && secretKey != null) {
            SessionCredentialsProvider sessionCredentialsProvider = new StaticSessionCredentialsProvider(accessKey.strip(), secretKey.strip());
            clientConfigurationBuilder.setCredentialProvider(sessionCredentialsProvider);
        }
        Map<String, List<Consumer>> consumerMap = getConsumerGroupByConsumerGroup();
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        PushConsumerBuilder pushConsumerBuilder = provider.newPushConsumerBuilder()
                .setClientConfiguration(clientConfigurationBuilder.build());
        consumerMap.forEach((consumerGroup, consumerList) -> initConsumerGroup(pushConsumerBuilder, consumerGroup, consumerList));
        log.info("初始化消费者结束");
    }

    private void initConsumerGroup(PushConsumerBuilder pushConsumerBuilder, String consumerGroup, List<Consumer> consumerList) {
        Map<String, PushConsumer> newRocketMQConsumerMap = new HashMap<>();
        Map<String, FilterExpression> subscriptionExpressions = new HashMap<>();
        Map<String, Consumer> consumerMap = new HashMap<>();
        for (Consumer consumer : consumerList) {
            String tag = consumer.getTag();
            FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
            subscriptionExpressions.put(consumer.getTopic(), filterExpression);
            consumerMap.put(consumer.getTopic(), consumer);
        }
        PushConsumer rocketMQConsumer;
        try {
            rocketMQConsumer = pushConsumerBuilder
                    .setConsumerGroup(consumerGroup)
                    .setSubscriptionExpressions(subscriptionExpressions)
                    .setMessageListener(messageView -> processMessage(consumerMap, messageView))
                    .build();
            newRocketMQConsumerMap.put(consumerGroup, rocketMQConsumer);
        } catch (ClientException e) {
            throw new RuntimeException("创建rocketMQ异常", e);
        }
        this.rocketMQConsumerMap = newRocketMQConsumerMap;
    }

    private ConsumeResult processMessage(Map<String, Consumer> consumerMap, MessageView messageView) {
        String topic = messageView.getTopic();
        Consumer consumer = consumerMap.get(topic);
        ByteBuffer body = messageView.getBody();
        byte[] bytes = new byte[body.remaining()];
        body.get(bytes);
        MessageContext messageContext = MessageContext.builder().body(bytes).messageView(messageView).build();
        return consumer.consume(messageContext);
    }
}
