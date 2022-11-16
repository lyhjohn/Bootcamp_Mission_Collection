package com.zerobase.userapi.domain;

import com.zerobase.userapi.service.SignUpCustomerService;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@NoArgsConstructor
public class SignUpForm {
	private String email;
	private String name;
	private String password;
	private LocalDate birth;
	private String phone;

	@Builder
	public SignUpForm(String email, String name, String password, LocalDate birth, String phone) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.birth = birth;
		this.phone = phone;
	}
}
