package com.zerobase.userapi.service;

import com.zerobase.userapi.client.MailgunClient;
import com.zerobase.userapi.client.mailgun.SendMailForm;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendService {

	private final MailgunClient mailgunClient;

	public String sendEmail() {
		SendMailForm form = SendMailForm.builder()
			.from("dladygks506@gmail.com")
			.to("dladygks506@naver.com")
			.subject("test email")
			.text("test success")
			.build();
		return mailgunClient.sendEmail(form).getBody();
	}

}
