package com.happy.rabbit.temolateannotation.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author happy
 */
@Configuration
public class DirectConfig {

    @Bean
    public Queue directQueue() {
        Queue queue = new Queue("direct",true);
        return queue;
    }
}
