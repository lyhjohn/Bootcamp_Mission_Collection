package mission.fastlmsmission.admin.dto.member;

import lombok.*;
import mission.fastlmsmission.member.entity.Member;
import mission.fastlmsmission.history.entity.History;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    LocalDateTime udt;
    boolean emailAuthYn;
    String emailAuthKey;
    LocalDateTime emailAuthDt;
    String resetPasswordKey;
    LocalDateTime resetPasswordLimitDt;
    boolean adminYn;

    long totalCount;
    long seq;

    String userStatus;
    String zipcode;
    String addr;
    String addrDetail;
    List<History> historyList = new ArrayList<>();

    public String getRegDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        return regDt != null ? regDt.format(formatter) : "";
    }

    public String getUdtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        return udt != null ? udt.format(formatter) : "";
    }


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
                .udt(member.getUdtDt())
                .zipcode(member.getZipcode())
                .addr(member.getAddr())
                .addrDetail(member.getAddrDetail())
                .historyList(member.getHistoryList())
                .build();
    }

    public static List<MemberDto> of(List<Member> memberList) {
        List<MemberDto> members = new ArrayList<>();
        memberList.forEach(m -> members.add(MemberDto.of(m)));
        return members;
    }

}
