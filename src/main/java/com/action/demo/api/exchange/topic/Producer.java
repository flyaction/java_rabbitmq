package com.action.demo.api.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author: action
 * @create: 2025/3/27 15:18
 **/
public class Producer {
    public static void main(String[] args) throws Exception {
        //1、创建连接工厂，并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2、通过连接工厂创建连接和mq建立连接
        Connection connection = connectionFactory.newConnection();

        //3、通过connection创建一个channel通道
        Channel channel = connection.createChannel();

        //4、声明
        String exchangeName = "test_topic_exchange";
        String routingKey1 = "user.save";
        String routingKey2 = "user.update";
        String routingKey3 = "user.delete.abc";

        //5、发送消息
        String msg = "Hello World Rabbitmq Topic Exchange Message...";
        channel.basicPublish(exchangeName, routingKey1, null, msg.getBytes());
        channel.basicPublish(exchangeName, routingKey2, null, msg.getBytes());
        channel.basicPublish(exchangeName, routingKey3, null, msg.getBytes());

        //6、关闭相关连接
        channel.close();
        connection.close();
    }
}
