package com.action.demo.api.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;
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

        //4、指定我们的消息投递模式：消息确认模式
        channel.confirmSelect();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.save";

        //5、通过channel发送数据
        //for (int i = 0; i < 5; i++){
            String msg = "Hello RabbitMQ Send confirm message!";
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
        //}

        //6、添加一个确认监听
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("----------no ack!------------");
            }
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("----------ack!------------");
            }
        });

    }
}
