package com.happy.rabbit.templateMq;

import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

/**
 * @author happy
 */
@Component
public class TemplateSendTest extends AbstractSendService {


    public static String exchange = "rabbitTemplate.exchange";

    private String routingKey = "routingKey.rabbitTemplate";

    private String queue = "rabbitTemplate.queue";


    public void sendMessage() {
        for (int i = 0; i < 100; i++) {
            this.send(exchange, routingKey, queue, "这是一个简单的发送: " + i);
        }
    }

    @Override
    public void handleConfirmCallback(String messageId, boolean ack, String cause) {
        System.out.println("cause----->" + cause);
    }

    @Override
    public void handleReturnCallback(Message message, int replyCode, String replyText, String routingKey) {
        System.out.println("message-->" + message + " routingKey-->" + routingKey);
    }
}
