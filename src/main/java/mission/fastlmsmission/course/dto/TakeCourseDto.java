package mission.fastlmsmission.course.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mission.fastlmsmission.course.entity.Course;
import mission.fastlmsmission.course.entity.TakeCourse;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TakeCourseDto {

    private Long id;

    private long courseId;
    private String userId;
    private long payPrice;
    private String status; // 수강신청, 결제완료, 수강취소
    private LocalDateTime regDt;

    private String userName;
    private String phone;
    private String subject;

    private long totalCount;
    long seq;

    public static TakeCourseDto of(TakeCourse takeCourse) {
        return TakeCourseDto.builder()
                .id(takeCourse.getId())
                .courseId(takeCourse.getCourseId())
                .userId(takeCourse.getUserId())
                .payPrice(takeCourse.getPayPrice())
                .status(takeCourse.getStatus())
                .regDt(takeCourse.getRegDt())
                .build();
    }

    public String getRegDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        return regDt != null ? this.regDt.format((formatter)) : "";
    }


}
