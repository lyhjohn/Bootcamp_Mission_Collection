package mission.fastlmsmission.member.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    private String email;
    private String userName;
    private String password;
    private String phone;

    private LocalDateTime regDt; // 가입 일자
    private boolean emailAuthYn; // 이메일 인증 여부
    private String emailAuthKey; // 이메일 인증 키

    private LocalDateTime emailAuthDt;
    private String resetPasswordKey; // uuid

    private LocalDateTime resetPasswordLimitDt;

    // 관리자 도메인을 새로 만들지, 멤버에 관리자여부 필드를 추가할지?
    // 등급에따라 회원 ROLE을 구분할건지
    private boolean adminYn;




    @Override
    public String toString() {
        return "Member{" +
                "email='" + email + '\'' +
                ", name='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phone + '\'' +
                '}';
    }
}
