package com.happy.rabbit.temolateannotation.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author happy
 */
@Component
public class DirectConsumer {

    @RabbitListener(queues = "direct")
    public void receive(String message) {
        System.out.println("message= " + message);
    }
}
