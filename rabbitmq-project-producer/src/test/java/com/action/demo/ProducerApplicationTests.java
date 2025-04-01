package com.action.demo;

import com.action.demo.entity.Order;
import com.action.demo.producer.RabbitSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ProducerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private RabbitSender rabbitSender;

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Test
	public void testSend1() throws Exception {
		Map<String, Object> properties = new HashMap<>();
		properties.put("number", 12345);
		properties.put("send_time", simpleDateFormat.format(new Date()));
		rabbitSender.send("Hello RabbitMQ For Spring Boot.", properties);
	}

	@Test
	public void testSend2() throws Exception {
		rabbitSender.sendOrder(new Order("123456", "RabbitMQ 订单消息"));
	}
}
