package com.happy.rabbit.templateMq;

import lombok.Data;

/**
 * @author happy
 */
@Data
public class MqMessage {

    private Object messageBody;

    private String messageId;

    private String exchangeName;

    private String queueName;

    private String routingKey;

}
