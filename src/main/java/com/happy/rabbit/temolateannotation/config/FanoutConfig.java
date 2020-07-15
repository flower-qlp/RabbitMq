package com.happy.rabbit.temolateannotation.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author happy
 */
@Configuration
public class FanoutConfig {

    @Bean(name = "Fanout.message1")
    public Queue queue1() {
        return new Queue("Fanout.message1",true);
    }

    @Bean(name = "Fanout.message2")
    public Queue queue2() {
        return new Queue("Fanout.message2",true);
    }

    @Bean(name = "Fanout.message4")
    public Queue queue4() {
        return new Queue("Fanout.message4",true);
    }

    @Bean(name = "Fanout.message3")
    public Queue queue3() {
        return new Queue("Fanout.message3",true);
    }

    @Bean
    public FanoutExchange exchangeFanout() {
        return new FanoutExchange("Fanout.exchange");
    }

    @Bean
    Binding binding1(@Qualifier("Fanout.message1") Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    Binding binding2(@Qualifier("Fanout.message2") Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    Binding binding3(@Qualifier("Fanout.message3") Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    Binding binding4(@Qualifier("Fanout.message4") Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

}
