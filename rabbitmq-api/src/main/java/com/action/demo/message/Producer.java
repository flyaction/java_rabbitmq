package com.action.demo.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.AMQBasicProperties;

import java.util.HashMap;
import java.util.Map;

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

        Map <String, Object> header = new HashMap<>();
        header.put("my1","111111");
        header.put("my2","222222");

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .expiration("10000")
                .headers(header)
                .build();

        //4、通过channel发送数据
        for (int i = 0; i < 5; i++){
            String msg = "Hello RabbitMQ!";
            channel.basicPublish("", "test001", properties, msg.getBytes());
        }

        //5、关闭相关连接
        channel.close();
        connection.close();

    }
}
