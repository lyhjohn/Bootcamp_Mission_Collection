package com.zerobase.userapi.application;

import static com.zerobase.userapi.exception.ErrorCode.ALREADY_REGISTER_USER;

import com.zerobase.userapi.client.MailgunClient;
import com.zerobase.userapi.client.mailgun.SendMailForm;
import com.zerobase.userapi.domain.SignUpForm;
import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.exception.CustomException;
import com.zerobase.userapi.exception.ErrorCode;
import com.zerobase.userapi.service.SignUpCustomerService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignUpApplication {

	private final MailgunClient mailgunClient;
	private final SignUpCustomerService signUpCustomerService;

	public void customerVerify(String email, String code) {
		signUpCustomerService.verifyEmail(email, code);
	}

	public String customerSignUp(SignUpForm form) {
		if (signUpCustomerService.isEmailExist(form.getEmail())) {
			throw new CustomException(ALREADY_REGISTER_USER);
		}

		Customer customer = signUpCustomerService.signUp(form);
		String code = getRandomCode();
		SendMailForm sendMailForm = SendMailForm.builder()
			.from("dladygks506@gmail.com")
			.to(form.getEmail())
			.subject("Verification Email")
			.text(getVerificationEmailBody(customer.getEmail(), customer.getName(), code))
			.build();
//		ResponseEntity<String> result = mailgunClient.sendEmail(sendMailForm);
//		log.info("Send mail result:{}", result.getBody());
		signUpCustomerService.changeCustomerValidateEmail(customer.getId(), code);
		return "회원 가입에 성공하였습니다.";
	}

	private String getRandomCode() {
		return RandomStringUtils.random(10, true, true);
	}

	private String getVerificationEmailBody(String email, String name, String code) {
		StringBuilder builder = new StringBuilder();
		return builder.append("Hello ").append(name)
			.append("! Please Click Link for verification.\n\n")
			.append("http://localhost:8081/signup/verify/customer?email=")
			.append(email)
			.append("&code=")
			.append(code).toString();
	}
}
