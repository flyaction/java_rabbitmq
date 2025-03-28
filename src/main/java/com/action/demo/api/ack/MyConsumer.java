package com.action.demo.api.ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @author: action
 * @create: 2025/3/28 15:42
 **/
public class MyConsumer extends DefaultConsumer {

    private Channel channel;

    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("--------consume message----------");
        System.out.println("body:" + new String(body));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if((Integer)properties.getHeaders().get("num") == 0){
            channel.basicNack(envelope.getDeliveryTag(), false, true);
        }else{
            //代表这条消息处理完了，你可以给我下一条了
            channel.basicAck(envelope.getDeliveryTag(), false);
        }
    }
}
