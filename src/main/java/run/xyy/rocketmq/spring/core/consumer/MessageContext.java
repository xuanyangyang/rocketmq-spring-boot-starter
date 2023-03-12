package run.xyy.rocketmq.spring.core.consumer;

import lombok.Builder;
import lombok.Data;
import org.apache.rocketmq.client.apis.message.MessageView;

/**
 * @author XYY
 * @since 1.0
 */
@Data
@Builder
public class MessageContext {
    private final byte[] body;
    private final MessageView messageView;
}
