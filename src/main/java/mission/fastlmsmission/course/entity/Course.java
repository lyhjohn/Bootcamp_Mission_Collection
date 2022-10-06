package mission.fastlmsmission.course.entity;

import lombok.*;
import mission.fastlmsmission.admin.entity.Category;
import mission.fastlmsmission.course.exception.CourseException;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
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
    private LocalDate saleEndDt;
    private LocalDateTime regDt;
    private LocalDateTime udtDt; // 업데이트 날짜

    private Long categoryId;

    private String fileName;
    private String urlFileName;

    public void setSaleEndDt(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            this.saleEndDt = LocalDate.parse(value, formatter);
        } catch (Exception e) {
            throw new CourseException("올바른 날짜를 입력해주세요 (yyyy-MM-dd");
        }
    }
}
