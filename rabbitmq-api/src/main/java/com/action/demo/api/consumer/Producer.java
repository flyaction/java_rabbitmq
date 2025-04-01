package com.action.demo.api.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author: action
 * @create: 2025/3/27 11:03
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

        //4、
        String exchangeName = "test_consumer_exchange";
        String routingKey = "consumer.save";

        //5、通过channel发送数据
        String msg = "Hello RabbitMQ Send consumer message!";
        for (int i = 0; i < 5; i++){
            channel.basicPublish(exchangeName, routingKey, true,null, msg.getBytes());
        }
    }
}
