package com.action.demo.api.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author: action
 * @create: 2025/3/27 15:18
 **/
public class Consumer {
    public static void main(String[] args) throws Exception {
        //1、创建连接工厂，并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);


        //2、通过连接工厂创建连接和mq建立连接
        Connection connection = connectionFactory.newConnection();

        //3、通过connection创建一个channel通道
        Channel channel = connection.createChannel();

        //4、声明一个队列
        String exchangeName = "test_topic_exchange";
        String exchangeType = "topic";
        String queueName = "test_topic_queue";
        String routingKey = "user.*"; //匹配一个词
        //String routingKey = "user.#";//匹配一个或多个词
        //表示声明一个交换机
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false,null);
        //表示声明一个队列
        channel.queueDeclare(queueName, false, false, false, null);
        //表示绑定队列和交换机
        channel.queueBind(queueName, exchangeName, routingKey);

        //5、消费消息
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
