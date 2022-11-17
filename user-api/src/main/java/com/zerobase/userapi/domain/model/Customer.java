package com.zerobase.userapi.domain.model;

import com.zerobase.userapi.domain.SignUpForm;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditOverride;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
// envers 라이브러리를 사용해서 BaseEntity의 데이터가 변경될때마다 로그로 변경이력을 볼 수 있음.
@AuditOverride(forClass = BaseEntity.class)
@ToString(callSuper = true)
public class Customer extends BaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String email;
	private String name;
	private String password;
	private LocalDate birth;

	@Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "올바른 휴대폰 번호를 입력해주세요.")
	private String phone;
	private LocalDateTime verifyExpiredAt;
	private String verificationCode;
	private boolean verify;

	public static Customer from(SignUpForm form) {
		return Customer.builder()
			// 소문자로 변환, 지역마다 UTF차이가 있으므로 통일을 위해 Locale.Root
			.email(form.getEmail().toLowerCase(Locale.ROOT))
			.password(form.getPassword())
			.name(form.getName())
			.birth(form.getBirth())
			.phone(form.getPhone())
			.verify(false)
			.build();
	}

	@Builder
	public Customer(String email, String name, String password, LocalDate birth, String phone,
		LocalDateTime verifyExpiredAt, String verificationCode, boolean verify) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.birth = birth;
		this.phone = phone;
		this.verifyExpiredAt = verifyExpiredAt;
		this.verificationCode = verificationCode;
		this.verify = verify;
	}
}
