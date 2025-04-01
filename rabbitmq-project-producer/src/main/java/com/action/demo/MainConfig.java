package com.action.demo;

import com.action.demo.convert.Obj2JsonConvert;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: action
 * @create: 2025/4/1 09:24
 **/
@Configuration
@ComponentScan("com.action.demo")
public class MainConfig {


    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory)
    {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        MessageListenerAdapter adapter = new MessageListenerAdapter();
        adapter.setMessageConverter(new Obj2JsonConvert());
        container.setMessageListener(adapter);
        return container;
    }
}
