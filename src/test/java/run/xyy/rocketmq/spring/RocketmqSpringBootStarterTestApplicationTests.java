package run.xyy.rocketmq.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.message.MessageBuilder;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.java.message.MessageBuilderImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class RocketmqSpringBootStarterTestApplicationTests {
    @Autowired
    private Producer producer;

    @Test
    void testSend() throws ClientException, InterruptedException {
        log.info("test send");
        MessageBuilder messageBuilder = new MessageBuilderImpl();
        Message message = messageBuilder
                .setTopic("test-topic")
                .setBody("hello-world".getBytes())
                .build();
        SendReceipt sendReceipt = producer.send(message);
        log.info("Send message successfully, messageId={}", sendReceipt.getMessageId());
        Thread.sleep(10 * 1000L);
    }

    @Test
    void testConsumer() throws InterruptedException {
        log.info("开始等待消息");
        Thread.sleep(30 * 1000);
        log.info("结束等待消息");
    }
}
