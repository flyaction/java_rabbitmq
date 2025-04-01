package com.action.demo.convert;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

/**
 * @author: action
 * @create: 2025/4/1 15:09
 **/
@Component
public class Obj2JsonConvert implements MessageConverter {


    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        System.err.println(111111);
        return new Message(JSON.toJSONBytes(o),messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.err.println(222222);
        return JSON.parse(message.getBody());
    }
}
