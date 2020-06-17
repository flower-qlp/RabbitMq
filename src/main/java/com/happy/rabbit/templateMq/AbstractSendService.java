package com.happy.rabbit.templateMq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @author happy
 * 消息发送封装
 */
public abstract class AbstractSendService implements RabbitTemplate.ConfirmCallback,
        RabbitTemplate.ReturnCallback {

    public final Log logger = LogFactory.getLog(this.getClass());


    @Autowired
    RabbitTemplate rabbitTemplate;

    public void send(String exchange, String routingKey, String queue, String content) {
        this.send(exchange, routingKey, queue, content, null, UUID.randomUUID().toString());
    }

    /**
     * 发送一天有过期时间的消息
     **/
    public void send(String exchange, String routingKey, String queue, String content, int expireTime) {
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(expireTime + "");
                return message;
            }
        };
        this.send(exchange, routingKey, queue, content, messagePostProcessor, UUID.randomUUID().toString());
    }

    public void send(String exchange, String routingKey, String queue, Object o,
                     MessagePostProcessor messagePostProcessor,
                     String messageId) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(StringUtils.isEmpty(messageId) ? UUID.randomUUID().toString() : messageId);
        MqMessage mqMessage = new MqMessage();
        mqMessage.setMessageBody(o);
        mqMessage.setMessageId(correlationData.getId());
        mqMessage.setExchangeName(exchange);
        mqMessage.setQueueName(queue);
        mqMessage.setRoutingKey(routingKey);

        if (StringUtils.isEmpty(messagePostProcessor)) {
            this.rabbitTemplate.convertAndSend(exchange, routingKey, mqMessage, correlationData);
        } else {
            this.rabbitTemplate.convertAndSend(exchange, routingKey, mqMessage, messagePostProcessor, correlationData);
        }
    }

    /**
     * 默认实现发送确认的处理方法
     * 子类需要重写该方法 实现自己的业务
     *
     * @param messageId
     * @param ack
     * @param cause
     **/
    public abstract void handleConfirmCallback(String messageId, boolean ack, String cause);

    /**
     * 默认发送匹配不到
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param routingKey
     **/
    public abstract void handleReturnCallback(Message message, int replyCode,
                                              String replyText, String routingKey);

    /**
     * @PostConstruct 修饰非静态方法 在service在加载时运行一次
     * 在构造方法之后 在init()方法之前
     **/
    @PostConstruct
    public final void init() {
        this.logger.info("sendService 初始化...");
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 确认回调方法
     *
     * @param correlationData
     * @param ack
     * @param cause
     **/
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        this.logger.info("confirm ----correlationData:" + correlationData.toString());
        this.handleConfirmCallback(correlationData.getId(), ack, cause);
    }

    /**
     * 匹配失败后的回调
     **/
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        this.logger.info("return----message:" + message);
        this.handleReturnCallback(message, replyCode, replyText, routingKey);
    }
}
