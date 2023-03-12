package run.xyy.rocketmq.spring.core.consumer;

import org.apache.rocketmq.client.apis.consumer.ConsumeResult;

/**
 * @author XYY
 * @since 1.0
 */
public interface Consumer {
    String getTopic();

    String getConsumerGroup();

    default String getTag() {
        return "*";
    }

    ConsumeResult consume(MessageContext messageContext);
}
