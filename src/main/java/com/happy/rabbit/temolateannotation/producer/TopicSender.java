package com.happy.rabbit.temolateannotation.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author happy
 */
@Component
public class TopicSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(String message){
        amqpTemplate.convertAndSend("topic.exchange","topic.message.one",message);
    }

}
