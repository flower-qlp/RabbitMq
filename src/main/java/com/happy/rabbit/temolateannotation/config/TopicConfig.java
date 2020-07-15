package com.happy.rabbit.temolateannotation.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * topic 发布模式
 *
 * @author happy
 **/
@Configuration
public class TopicConfig {

    /**
     * 队列1
     **/
    @Bean(name = "messageOne")
    Queue queueOne() {
        return new Queue("topic.message.one", true);
    }

    /**
     * 队列2
     **/
    @Bean(name = "messageTwo")
    Queue queueTwo() {
        return new Queue("topic.message.two", true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topic.exchange");
    }

    /**
     * @Qualifier 合格者 标注 哪个类才是我们需要的
     **/
    @Bean
    Binding binding(@Qualifier("messageOne") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("topic.message.one");
    }

    /**
     * * 标识一个词 #标识0个或者多个
     * 发送topic
     **/
    @Bean
    Binding bindings(@Qualifier("messageTwo") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("topic.#");
    }
}
