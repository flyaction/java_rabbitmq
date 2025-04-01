package com.action.demo.consumer;

import com.action.demo.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: action
 * @create: 2025/4/1 10:26
 **/
@Component
public class RabbitReceiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-1", durable = "true"),
            exchange = @Exchange(value = "exchange-1}", type = "topic", durable = "true",ignoreDeclarationExceptions = "true"),
            key = "springboot.*"
    ))
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        System.err.println("------------------------");
        System.err.println("消费端Payload："+message.getPayload());
        System.err.println("消费端Payload：" + new String((byte[]) message.getPayload()));
        Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        //手动ACK
        channel.basicAck(deliveryTag,false);
    }

    /**
     * spring.rabbitmq.listener.order.queue.name=queue-2
     * spring.rabbitmq.listener.order.queue.durable=true
     * spring.rabbitmq.listener.order.exchange.name=exchange-1
     * spring.rabbitmq.listener.order.exchange.durable=true
     * spring.rabbitmq.listener.order.exchange.type=topic
     * spring.rabbitmq.listener.order.exchange.ignore-declaration-exceptions=true
     * spring.rabbitmq.listener.order.key=springboot.*
     *
     */

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}", durable = "${spring.rabbitmq.listener.order.queue.durable}"),
            exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}", type = "${spring.rabbitmq.listener.order.exchange.type}", durable = "${spring.rabbitmq.listener.order.exchange.durable}",ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignore-declaration-exceptions}"),
            key = "${spring.rabbitmq.listener.order.key}"
    ))
    @RabbitHandler
    public void onOrderMessage(Message message, Channel channel) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = objectMapper.readValue((byte[]) message.getPayload(), Order.class);
        System.err.println("------------------------");
        System.err.println("消费端order：" + order.getId());
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        // 手动 ACK
        channel.basicAck(deliveryTag, false);
    }

//    public void onOrderMessage(@Payload com.action.demo.entity.Order order,
//                               Channel channel,
//                               @Headers Map<String,Object> headers) throws Exception {
//        System.err.println("------------------------");
//        //System.err.println("消费端order："+order.getId());
//        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
//        //手动ACK
//        channel.basicAck(deliveryTag,false);
//    }




}
