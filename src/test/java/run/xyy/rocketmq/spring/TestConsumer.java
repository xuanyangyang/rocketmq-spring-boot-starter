package run.xyy.rocketmq.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.springframework.stereotype.Component;
import run.xyy.rocketmq.spring.core.consumer.Consumer;
import run.xyy.rocketmq.spring.core.consumer.MessageContext;

/**
 * @author XYY
 * @since 1.0
 */
@Component
@Slf4j
public class TestConsumer implements Consumer {
    @Override
    public String getTopic() {
        return "test-topic";
    }

    @Override
    public String getConsumerGroup() {
        return "test-consume";
    }

    @Override
    public ConsumeResult consume(MessageContext messageContext) {
        byte[] body = messageContext.getBody();
        String message = new String(body);
        log.info("收到消息:{}", message);
        return ConsumeResult.SUCCESS;
    }
}
