package com.zerobase.cms.user_api.application;

import static com.zerobase.cms.user_api.exception.ErrorCode.ALREADY_REGISTER_USER;

import com.zerobase.cms.user_api.client.MailgunClient;
import com.zerobase.cms.user_api.client.mailgun.SendMailForm;
import com.zerobase.cms.user_api.domain.SignUpForm;
import com.zerobase.cms.user_api.domain.model.Customer;
import com.zerobase.cms.user_api.domain.model.Seller;
import com.zerobase.cms.user_api.exception.CustomException;
import com.zerobase.cms.user_api.service.customer.SignUpCustomerService;
import com.zerobase.cms.user_api.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignUpApplication {

	private final MailgunClient mailgunClient;
	private final SignUpCustomerService signUpCustomerService;
	private final SellerService sellerService;

	public void customerVerify(String email, String code) {
		signUpCustomerService.verifyEmail(email, code);
	}

	public void sellerVerify(String email, String code) {
		sellerService.verifyEmail(email, code);
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
			.text(getVerificationEmailBody(customer.getEmail(), customer.getName(), "Customer", code))
			.build();
//		ResponseEntity<String> result = mailgunClient.sendEmail(sendMailForm);
//		log.info("Send mail result:{}", result.getBody());
		signUpCustomerService.changeCustomerValidateEmail(customer.getId(), code);
		return "회원 가입에 성공하였습니다.";
	}

	private String getRandomCode() {
		return RandomStringUtils.random(10, true, true);
	}

	public String sellerSignUp(SignUpForm form) {
		if (sellerService.isEmailExist(form.getEmail())) {
			throw new CustomException(ALREADY_REGISTER_USER);
		}

		Seller seller = sellerService.signUp(form);
		String code = getRandomCode();
		SendMailForm sendMailForm = SendMailForm.builder()
			.from("dladygks506@gmail.com")
			.to(form.getEmail())
			.subject("Verification Email")
			.text(getVerificationEmailBody(seller.getEmail(), seller.getName(), "Seller", code))
			.build();
//		ResponseEntity<String> result = mailgunClient.sendEmail(sendMailForm);
//		log.info("Send mail result:{}", result.getBody());
		sellerService.changeSellerValidateEmail(seller.getId(), code);
		return "회원 가입에 성공하였습니다.";
	}

	private String getVerificationEmailBody(String email, String name, String type, String code) {
		StringBuilder builder = new StringBuilder();
		return builder.append("Hello ").append(name)
			.append("! Please Click Link for verification.\n\n")
			.append("http://localhost:8081/signUp/customer/verify?email=")
			.append(email)
			.append("&code=")
			.append(code).toString();
	}
}
