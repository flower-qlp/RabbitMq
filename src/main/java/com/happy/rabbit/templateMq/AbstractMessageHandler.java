package com.happy.rabbit.templateMq;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author happy
 * 消息接收处理类
 **/
public abstract class AbstractMessageHandler implements ChannelAwareMessageListener {

    private final Log logger = LogFactory.getLog(this.getClass());

    private ConcurrentHashMap<String, AcknowledgeMode> ackMap = new ConcurrentHashMap<>();

    public final void setAck(String queue, AcknowledgeMode ack) {
        this.ackMap.put(queue, ack);
    }

    /**
     * 用户定义消息处理方法
     *
     * @param message
     * @param channel
     **/
    public abstract void handleMessage(String message, Channel channel);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        this.logger.info("接收到发送的消息.......");

        //业务是否成功
        boolean handleResult = false;
        //消息处理标识
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //获取队列名称
        String queue = message.getMessageProperties().getConsumerQueue();

        MqMessage mqMessage = null;

        try {
            String msg = new String(message.getBody());
            mqMessage = JSONObject.parseObject(msg, MqMessage.class);
            //自定义处理业务
            this.handleMessage(JSONObject.toJSONString(mqMessage.getMessageBody()), channel);
            handleResult = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //消息处理失败 采取的措施
        this.onMessageCompleted(mqMessage, queue, channel, deliveryTag, handleResult);
    }


    /***
     * RetryTemplate 重试
     * 定义好重试的策略：重试的触发条件，重试次数，时间间隔等要素
     *
     * **/
    private void onMessageCompleted(MqMessage mqMessage, String queue,
                                    Channel channel, long deliveryTag,
                                    boolean handleResult) {
        this.logger.info("消息：" + mqMessage.getMessageBody().toString() + " 处理完成！！！");
        if (!handleResult) {
            //业务失败
            this.logger.info("消息：" + mqMessage.getMessageBody().toString() + " 签收失败！！！");
            return;
        }
        AcknowledgeMode ack = this.ackMap.get(queue);
        if (ack.isManual()) {
            //重试5次
            int retryTimes = 5;
            //重试发送
            RetryTemplate retryTemplate = new RetryTemplate();
            SimpleRetryPolicy policy = new SimpleRetryPolicy();
            //重试次数
            policy.setMaxAttempts(retryTimes);
            retryTemplate.setRetryPolicy(policy);

            try {
                Integer result = retryTemplate.execute(new RetryCallback<Integer, Throwable>() {
                    Integer count = 0;

                    @Override
                    public Integer doWithRetry(RetryContext retryContext) {
                        //开始重试
                        try {
                            channel.basicAck(deliveryTag, false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        AbstractMessageHandler.this.logger.info("消息：" + mqMessage.getMessageBody().toString() + " 已经签收！！");

                        return ++this.count;
                    }
                }, new RecoveryCallback<Integer>() {
                    @Override
                    public Integer recover(RetryContext retryContext) {
                        //重试多次依然失败
                        AbstractMessageHandler.this.logger.info("消息" + mqMessage.toString() + "签收失败");
                        return Integer.MAX_VALUE;
                    }
                });

                if (result.intValue() <= retryTimes) {
                    //消息发送成功
                } else {
                    //消息发送失败
                }

            } catch (Exception e) {
                this.logger.error("消息:" + mqMessage.getMessageBody().toString() + " 签收异常！！！");
            } catch (Throwable throwable) {
                this.logger.error("消息:" + mqMessage.getMessageBody().toString() + " 签收异常！！！");
                throwable.printStackTrace();
            }

        } else {
            this.logger.info("消息：" + mqMessage.getMessageBody().toString() + " 已经自动签收！！！");
        }

    }

}
