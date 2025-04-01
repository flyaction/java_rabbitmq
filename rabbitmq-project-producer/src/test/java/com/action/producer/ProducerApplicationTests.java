package com.action.producer;

import com.action.producer.producer.RabbitSender;
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
}
