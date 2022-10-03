package mission.fastlmsmission.course.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mission.fastlmsmission.course.entity.Course;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
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
    long totalCount;
    long seq;



    public static CourseDto of(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .imagePath(course.getImagePath())
                .keyword(course.getKeyword())
                .contents(course.getContents())
                .price(course.getPrice())
                .salePrice(course.getSalePrice())
                .saleEndDt(course.getSaleEndDt())
                .summary(course.getSummary())
                .subject(course.getSubject())
                .regDt(course.getRegDt())
                .udtDt(course.getUdtDt())
                .build();
    }

    public static List<CourseDto> ofList(List<Course> courses) {
        if (!CollectionUtils.isEmpty(courses)) {
            List<CourseDto> courseList = new ArrayList<>();
            courses.forEach(course -> courseList.add(CourseDto.of(course)));
            return courseList;
        }
        return null;
    }
}
