package com.happy.rabbit.temolateannotation.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author happy
 **/
@Component
public class FanoutConsumer {

    @RabbitListener(queues = "Fanout.message1")
    public void consumer1(String s) {
        System.out.println("message1=" + s);
    }

    @RabbitListener(queues = "Fanout.message2")
    public void consumer2(String s) {
        System.out.println("message2=" + s);
    }

    @RabbitListener(queues = "Fanout.message3")
    public void consumer3(String s) {
        System.out.println("message3=" + s);
    }

    @RabbitListener(queues = "Fanout.message4")
    public void consumer4(String s) {
        System.out.println("message4=" + s);
    }

}
