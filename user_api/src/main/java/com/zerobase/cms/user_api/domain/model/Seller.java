package com.zerobase.cms.user_api.domain.model;

import com.zerobase.cms.user_api.domain.SignUpForm;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Seller extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String name;
	private String password;
	private LocalDate birth;
	private String phone;
	private LocalDateTime verifyExpiredAt;
	private String verificationCode;
	private boolean verify;
	private int balance;

	public static Seller from(SignUpForm form) {
		return Seller.builder()
			// 소문자로 변환, 지역마다 UTF차이가 있으므로 통일을 위해 Locale.Root
			.email(form.getEmail().toLowerCase(Locale.ROOT))
			.password(form.getPassword())
			.name(form.getName())
			.birth(form.getBirth())
			.phone(form.getPhone())
			.verify(false)
			.build();
	}
}
