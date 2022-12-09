package com.zerobase.cms.user_api.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
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

