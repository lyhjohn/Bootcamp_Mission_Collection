package com.zerobase.userapi.service;

import com.zerobase.userapi.client.MailgunClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailSendServiceTest {

	@Autowired
	private MailgunClient mailgunClient;

	@Test
	public void EmailTest() {
		// need test code
//		mailgunClient.sendEmail();
//		String response = emailSendService.sendEmail();
//		System.out.println(response);
	}

}
