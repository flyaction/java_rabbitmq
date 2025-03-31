package com.action.demo.api.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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
        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.save";

        //5、通过channel发送数据
        String msg = "Hello RabbitMQ Send DLX Message!";
        for (int i = 0; i < 5; i++){
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .expiration("10000")
                    .build();

            channel.basicPublish(exchangeName, routingKey, true,properties, msg.getBytes());
        }
    }
}
