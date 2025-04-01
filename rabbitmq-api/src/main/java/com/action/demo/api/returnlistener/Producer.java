package com.action.demo.api.returnlistener;

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
        String exchangeName = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "abc.save";

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routerKey, AMQP.BasicProperties basicProperties, byte[] body) throws IOException {
                System.err.println("-----------handle return-------------");
                System.err.println("replyCode："+ replyCode);
                System.err.println("replyText："+ replyText);
                System.err.println("exchange："+ exchange);
                System.err.println("routerKey："+ routerKey);
                System.err.println("basicProperties："+ basicProperties);
                System.err.println("body："+ new String(body));
            }
        });

        //5、通过channel发送数据
        String msg = "Hello RabbitMQ Send confirm message!";
        //channel.basicPublish(exchangeName, routingKey, true,null, msg.getBytes());

        channel.basicPublish(exchangeName, routingKeyError, true,null, msg.getBytes());

    }
}
