package com.happy.rabbit.templateMq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author happy
 * 队列设置监听基类
 */
@Component
public class MessageListen {

    public final Log logger= LogFactory.getLog(this.getClass());


    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * SimpleMessageListenerContainer 容器设置监听消息队列
     * **/
    public void addMessageLister(String queue,
                                 AbstractMessageHandler messageHandler,
                                 boolean isAck) throws Exception{
        SimpleMessageListenerContainer container=new SimpleMessageListenerContainer();
        container.setConnectionFactory(this.connectionFactory);
        container.setQueueNames(queue);
        //默认无check
        AcknowledgeMode ack=AcknowledgeMode.NONE;
        if(isAck){
            //需要手动check
            ack=AcknowledgeMode.MANUAL;
        }
        messageHandler.setAck(queue,ack);
        container.setAcknowledgeMode(ack);
        //MessageListenerAdapter 消息处理适配器 交给messageHandler处理消息
        MessageListenerAdapter adapter=new MessageListenerAdapter(messageHandler);
        container.setMessageListener(adapter);
        container.start();
        this.logger.info("------ 已成功监听异步消息触发通知队列：" + queue + " ------");
    }




}
