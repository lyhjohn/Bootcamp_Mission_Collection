package mission.fastlmsmission.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mission.fastlmsmission.admin.dto.member.MemberDto;
import mission.fastlmsmission.history.entity.History;
import mission.fastlmsmission.member.entity.Member;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDto {

    private Long id;
    private LocalDateTime loginDt;
    private String ip;
    private String userAgent;

    private Member member;

    public static HistoryDto of(History history) {
        return HistoryDto.builder()
                .id(history.getId())
                .loginDt(history.getLoginDt())
                .ip(history.getIp())
                .userAgent(history.getUserAgent())
                .member(history.getMember())
                .build();
    }


    public String getLoginDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return this.loginDt != null ? loginDt.format(formatter) : "";
    }



}
