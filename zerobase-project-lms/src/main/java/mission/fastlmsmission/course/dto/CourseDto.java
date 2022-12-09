package mission.fastlmsmission.course.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mission.fastlmsmission.course.entity.Course;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    Long id;

    String imagePath;
    String keyword;
    String subject;

    String summary;

    String contents;
    Long price;
    Long salePrice;
    LocalDate saleEndDt;
    LocalDateTime regDt;
    LocalDateTime udtDt; // 업데이트 날짜
    long totalCount;
    long seq;
    Long categoryId;
    String fileName;
    String urlFileName;


    public static CourseDto of(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .categoryId(course.getCategoryId())
                .imagePath(course.getImagePath())
                .keyword(course.getKeyword())
                .subject(course.getSubject())
                .summary(course.getSummary())
                .contents(course.getContents())
                .price(course.getPrice())
                .salePrice(course.getSalePrice())
                .saleEndDt(course.getSaleEndDt())
                .regDt(course.getRegDt())
                .udtDt(course.getUdtDt())
                .urlFileName(course.getUrlFileName())
                .fileName(course.getFileName())
                .build();
    }

    public static List<CourseDto> of(List<Course> courses) {
        if (courses == null) {
            return null;
        }

        List<CourseDto> courseList = new ArrayList<>();
        courses.forEach(course -> courseList.add(CourseDto.of(course)));
        return courseList;
    }
}
