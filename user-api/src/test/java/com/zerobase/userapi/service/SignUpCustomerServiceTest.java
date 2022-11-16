package com.zerobase.userapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.zerobase.userapi.domain.SignUpForm;
import com.zerobase.userapi.domain.model.Customer;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class SignUpCustomerServiceTest {

	@Autowired
	private SignUpCustomerService service;

	@Test
	void signUp() {
		SignUpForm form = SignUpForm.builder()
			.name("user")
			.birth(LocalDate.now())
			.email("dladygks506@naver.com")
			.password("1234")
			.phone("01012345678")
			.build();
		Customer c = service.signUp(form);
		System.out.println("c = " + c);
		assertThat(c.getId()).isNotNull();
	}
}