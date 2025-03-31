package com.action.demo.api.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

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

        //这就是一个普通的交换机和队列以及路由
        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.#";
        String queueName = "test_dlx_queue";

        //4、 声明一个交换机和队列后进行绑定设置，最后制定路由key
        channel.exchangeDeclare(exchangeName, "topic", true,false,null);

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "dlx.exchange");
        //这个arguments就是死信队列的参数
        channel.queueDeclare(queueName,  true, false, false, arguments);

        //建立绑定关系
        channel.queueBind(queueName, exchangeName, routingKey);

        //要进行死信队列的声明
        channel.exchangeDeclare("dlx.exchange", "topic", true,false,null);
        channel.queueDeclare("dlx.queue",  true, false, false, null);
        channel.queueBind("dlx.queue", "dlx.exchange", "#");


        //5、消费消息
        channel.basicConsume(queueName, true, new MyConsumer(channel));

    }
}
