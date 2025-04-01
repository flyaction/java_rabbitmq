package com.action.demo.api.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author: action
 * @create: 2025/3/27 11:03
 **/
public class Consumer {

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

        String exchangeName = "test_ack_exchange";
        String routingKey = "ack.#";
        String queueName = "test_ack_queue";

        //4、 声明一个交换机和队列后进行绑定设置，最后制定路由key
        channel.exchangeDeclare(exchangeName, "topic", true,false,null);
        channel.queueDeclare(queueName,  true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        //5、消费消息
        //限流方式 1：autoAck=false 确认机制，手动确认
        channel.basicConsume(queueName, false, new MyConsumer(channel));

    }
}
