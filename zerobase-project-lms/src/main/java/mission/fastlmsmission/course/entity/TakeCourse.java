package mission.fastlmsmission.course.entity;

import lombok.*;
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
public class TakeCourse implements TakeCourseCode {

    @Id
    @GeneratedValue
    private Long id;

    private long courseId;
    private String userId;
    private long payPrice;
    private String status; // 수강신청, 결제완료, 수강취소
    private LocalDateTime regDt;
}
