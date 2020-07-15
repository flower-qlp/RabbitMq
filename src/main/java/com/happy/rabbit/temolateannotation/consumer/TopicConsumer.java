package com.happy.rabbit.temolateannotation.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author happy
 **/
@Component
public class TopicConsumer {

    @RabbitListener(queues = "topic.message.one")
    public void receive1(String s) {
        System.out.println("message1=" + s);
    }

    @RabbitListener(queues = "topic.message.two")
    public void receive2(String s) {
        System.out.println("message2=" + s);
    }

}
