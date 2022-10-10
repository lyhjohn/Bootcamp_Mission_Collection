package mission.fastlmsmission.member.entity;

import lombok.*;
import mission.fastlmsmission.history.entity.History;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements MemberCode{

    @Id
    private String email;
    private String userName;
    private String password;
    private String phone;

    private LocalDateTime regDt; // 가입 일자
    private LocalDateTime udtDt; // 가입 일자
    private boolean emailAuthYn; // 이메일 인증 여부
    private String emailAuthKey; // 이메일 인증 키

    private LocalDateTime emailAuthDt;
    private String resetPasswordKey; // uuid

    private LocalDateTime resetPasswordLimitDt;

    // 관리자 도메인을 새로 만들지, 멤버에 관리자여부 필드를 추가할지?
    // 등급에따라 회원 ROLE을 구분할건지
    private boolean adminYn;
    
    private String userStatus; //이용가능, 정지

    private String zipcode;
    private String addr;
    private String addrDetail;

    private LocalDateTime loginDt;
    @OneToMany(mappedBy = "member")
    private List<History> historyList = new ArrayList<>();



    public void setHistoryList(History history) {
        this.historyList.add(history);
        history.setMember(this);
    }


    @Override
    public String toString() {
        return "Member{" +
                "email='" + email + '\'' +
                ", name='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phone + '\'' +
                '}';
    }


    public Member(LocalDateTime loginDt) {
        this.loginDt = loginDt;
    }
}
