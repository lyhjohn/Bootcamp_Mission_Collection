package mission.fastlmsmission.admin.dto;

import lombok.*;
import mission.fastlmsmission.member.entity.Member;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    String email;
    String userName;
    String password;
    String phone;
    LocalDateTime regDt;
    boolean emailAuthYn;
    String emailAuthKey;
    LocalDateTime emailAuthDt;
    String resetPasswordKey;
    LocalDateTime resetPasswordLimitDt;
    boolean adminYn;

    long totalCount;
    long seq;

    String userStatus;



    public static MemberDto of(Member member) {
        return MemberDto.builder()
                .email(member.getEmail())
                .userName(member.getUserName())
                .phone(member.getPhone())
                .regDt(member.getRegDt())
                .emailAuthDt(member.getEmailAuthDt())
                .emailAuthKey(member.getEmailAuthKey())
                .resetPasswordKey(member.getResetPasswordKey())
                .resetPasswordLimitDt(member.getResetPasswordLimitDt())
                .emailAuthYn(member.isEmailAuthYn())
                .adminYn(member.isAdminYn())
                .userStatus(member.getUserStatus())
                .build();
    }



}
