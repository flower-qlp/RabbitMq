package com.happy.rabbit.producer;


import com.happy.rabbit.utils.Property;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * topic-主题模式
 * # 表示0个或多个关键字  * 表示一个关键字
 *
 * @author happy
 ***/
public class Sender {

    public static final String QUEUE_NAME = "test_topic_queue";

    public static String vHost = "47.104.147.118";

    public static String username = "happy";

    public static String password = "L123456";

    public static int port = 15672;

    public static String exchange = "test.topic";

    private static String routingKey="key.sender1";


    public static void sendMessage(String message) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("happy");
        factory.setHost(vHost);
        factory.setUsername(username);
        factory.setPassword(password);
        //factory.setPort(port);
        //获取连接
        Connection connection = factory.newConnection();
        //创建channel
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchange, "topic");
        //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicQos(4);

        channel.basicPublish(exchange, routingKey, null, message.getBytes());

        channel.close();
        connection.close();

    }

    public static void main(String[] args) throws Exception {
        while (true) {
            int time = (int) (Math.random() * 10000);
            sendMessage("你有" + time + "个苹果！！！！");
            System.out.println("你有" + time + "个苹果！！！！已经发送");
            Thread.sleep(time);
        }

    }

}
