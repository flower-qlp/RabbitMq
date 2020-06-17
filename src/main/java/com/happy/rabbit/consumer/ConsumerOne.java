package com.happy.rabbit.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;

import static com.happy.rabbit.producer.Sender.*;

/**
 * @author happy
 */
public class ConsumerOne {

    /**
     * routingKey 和bindingkey 实际上是一个  主要是为了区分 producer和consumer
     **/
    private static String bindingKey = "key.#";


    public static void getMessage() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("happy");
        factory.setHost(vHost);
        factory.setUsername(username);
        factory.setPassword(password);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //同一时间  发送几条数据给消费者
        //与basicAck 连用
        channel.basicQos(4);

        channel.queueBind(QUEUE_NAME, exchange, bindingKey);

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Consumer is accepted:" + message);
                int time = (int) (Math.random() * 10000);
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //手动确认消息
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        while (true) {
            //回调
            channel.basicConsume(QUEUE_NAME, false, consumer);
            Thread.sleep(2000);
        }

    }


    public static void main(String[] args) throws Exception {
        getMessage();
    }
}
