package mission.fastlmsmission.course.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    private String imagePath;
    private String keyword;
    private String subject;

    @Column(length = 1000)
    private String summary;

    @Lob // 대용량 데이터 저장(longtext 타입)
    private String contents;
    private Long price;
    private Long salePrice;
    private LocalDateTime saleEndDt;
    private LocalDateTime regDt;
    private LocalDateTime udtDt; // 업데이트 날짜
}
