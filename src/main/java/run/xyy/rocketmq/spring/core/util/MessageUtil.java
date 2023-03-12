package run.xyy.rocketmq.spring.core.util;

import org.apache.rocketmq.client.apis.message.MessageBuilder;
import org.apache.rocketmq.client.java.message.MessageBuilderImpl;

/**
 * @author XYY
 * @since 1.0
 */
public class MessageUtil {
    public static MessageBuilder newMessage() {
        return new MessageBuilderImpl();
    }
}
