package com.zerobase.userapi.client;

import com.zerobase.userapi.client.mailgun.SendMailForm;
import feign.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mailgun", url = "https://api.mailgun.net/v3/")
@Qualifier("mailgun")
public interface MailgunClient {

	@PostMapping("sandboxa701087dfae54b5b8e21f29907a8ad5f.mailgun.org/messages")
	ResponseEntity<String> sendEmail(@SpringQueryMap SendMailForm form);

}
