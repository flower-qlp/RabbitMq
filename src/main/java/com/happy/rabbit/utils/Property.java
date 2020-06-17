package com.happy.rabbit.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Property {

    @Value("${spring.rabbitmq.host}")
    private String vHost;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Value("${mq.config.exchange}")
    private String exchange;
}
