package com.happy.rabbit.templateMq;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static com.happy.rabbit.producer.Sender.*;

/**
 * @author happy
 */
@Configuration
public class RabbitMqConfig {

    /**
     * 发送失败是否回调给发送者
     **/
    @Value("${spring.rabbitmq.template.mandatory:false}")
    private Boolean mandatory;

    /**
     * 是否手动确认
     **/
    @Value("${spring.rabbitmq.publisher-confirms:false}")
    private Boolean publisherConfirms;

    /**
     * 如果mandatory设置成true，该值也是true
     **/
    @Value("${spring.rabbitmq.publisher-returns:false}")
    private Boolean publisherReturns;

    /**
     * 初始化connectionFactory的属性
     **/
    @Bean
    public ConnectionFactory connectionFactory() {
        /**
         * rabbitmq 连接池
         * **/
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setAddresses(vHost);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        cachingConnectionFactory.setVirtualHost("happy");
        //消息设置成回调 一下必须设置成true
        cachingConnectionFactory.setPublisherConfirms(this.publisherConfirms);
        cachingConnectionFactory.setPublisherReturns(this.publisherReturns);
        return cachingConnectionFactory;
    }

    @Bean(value = "templateExchange")
    public TopicExchange templateExchange() {
        return new TopicExchange(exchange);
    }

    /**
     * @Scope SCOPE_PROTOTYPE 多例
     ***/
    @Bean(name = "rabbitTemplate")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(this.connectionFactory());
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }


}
