package com.zerobase.userapi.domain;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
