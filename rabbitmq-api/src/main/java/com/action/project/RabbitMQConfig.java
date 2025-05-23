package com.action.project;

import com.action.project.adapter.MessageDelegate;
import com.action.project.convert.ImageMessageConverter;
import com.action.project.convert.PDFMessageConverter;
import com.action.project.convert.TextMessageConverter;
import com.action.project.entity.Order;
import com.action.project.entity.Packaged;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: action
 * @create: 2025/3/31 09:43
 **/
@Configuration
@ComponentScan({"com.action.project"})
public class RabbitMQConfig {
    @Bean
    public ConnectionFactory connectionFactory() {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;

    }
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        RabbitAdmin rabbitAdmin =  new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * Fanout: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange: 通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     * @return
     */
    @Bean
    public TopicExchange exchange001(){
        return new TopicExchange("topic001",true,false);
    }

    @Bean
    public Queue queue001(){
        //队列持久
        return new Queue("queue001",true);
    }

    @Bean
    public Binding binding001(){
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }

    @Bean
    public TopicExchange exchange002(){
        return new TopicExchange("topic002",true,false);
    }

    @Bean
    public Queue queue002(){
        //队列持久
        return new Queue("queue002",true);
    }

    @Bean
    public Binding binding002(){
        return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
    }


    @Bean
    public Queue queue003(){
        //队列持久
        return new Queue("queue003",true);
    }

    @Bean
    public Binding binding003(){
        return BindingBuilder.bind(queue003()).to(exchange001()).with("mq.*");
    }

    @Bean
    public Queue queue_image(){
        return new Queue("image_queue",true);
    }

    @Bean
    public Queue queue_pdf(){
        return new Queue("pdf_queue",true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(queue001(),queue002(),queue003(),queue_image(),queue_pdf());
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        container.setDefaultRequeueRejected(false);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setExposeListenerChannel(true);
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });
//        container.setMessageListener(new ChannelAwareMessageListener(){
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                String msg = new String(message.getBody());
//                System.out.println("message:"+msg);
//            }
//        });

        /**
         * 1、适配器方式，默认是有自己的方法名字的:handleMessage
         * 可以自己指定一个方法名字: consumeMessage
         * 可以添加一个转换器，从字节数组转换为String
         */
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        adapter.setMessageConverter(new TextMessageConverter());
//        container.setMessageListener(adapter);

        /**
         * 2、适配器方式，我们的队列名称和方法名称 也可以进行一一匹配
         */
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setMessageConverter(new TextMessageConverter());
//        Map<String,String> queueOrTagToMethodName = new HashMap<>();
//        queueOrTagToMethodName.put("queue001","method1");
//        queueOrTagToMethodName.put("queue002","method2");
//        adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
//        container.setMessageListener(adapter);

//        /**
//         * 1.1 支持json的转换器
//         */
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);

        /**
         * 1.2 DefaultJackson2JavaTypeMapper + jackson2JsonMessageConverter
         */
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
//        javaTypeMapper.setTrustedPackages("*");
//        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);

        /**
         * 1.3 DefaultJackson2JavaTypeMapper + jackson2JsonMessageConverter 支持java对象多映射转换
         */
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
//        Map<String, Class<?>> idClass = new HashMap<>();
//        idClass.put("order", Order.class);
//        idClass.put("packaged", Packaged.class);
//        javaTypeMapper.setIdClassMapping(idClass);
//        javaTypeMapper.setTrustedPackages("*");
//        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);

        /**
         * 1.4 ext convert
         */
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();

        TextMessageConverter textConvert = new TextMessageConverter();
        converter.addDelegate("text", textConvert);
        converter.addDelegate("html/text", textConvert);
        converter.addDelegate("xml/text", textConvert);
        converter.addDelegate("text/plain", textConvert);

        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        converter.addDelegate("json", jsonConverter);
        converter.addDelegate("application/json",jsonConverter);

        ImageMessageConverter imageConverter = new ImageMessageConverter();
        converter.addDelegate("image", imageConverter);
        converter.addDelegate("image/png", imageConverter);

        PDFMessageConverter pdfConverter = new PDFMessageConverter();
        converter.addDelegate("pdf", pdfConverter);

        adapter.setMessageConverter(converter);
        container.setMessageListener(adapter);



        return container;
    }

}
