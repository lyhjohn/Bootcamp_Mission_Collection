package mission.fastlmsmission.history.entity;

import lombok.*;
import mission.fastlmsmission.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class History {
    @GeneratedValue
    @Id
    private Long id;

    private LocalDateTime loginDt;
    private String ip;
    private String userAgent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static History setHistory(String ip, String userAgent) {
        History history = new History();
        history.setLoginDt(LocalDateTime.now());
        history.setIp(ip);
        history.setUserAgent(userAgent);
        return history;
    }

    public String getLoginDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd  HH:mm");
        return loginDt != null ? loginDt.format(formatter) : "";
    }
}
