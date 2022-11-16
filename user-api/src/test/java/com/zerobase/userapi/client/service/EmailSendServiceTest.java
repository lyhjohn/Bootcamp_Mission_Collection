package com.zerobase.userapi.client.service;

import com.zerobase.userapi.service.EmailSendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendServiceTest {

	@Autowired
	EmailSendService emailSendService;

	@Test
	public void emailTest() {
		String response = emailSendService.sendEmail();
		System.out.println("response = " + response);
	}

}