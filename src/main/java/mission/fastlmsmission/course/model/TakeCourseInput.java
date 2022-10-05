package mission.fastlmsmission.course.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class TakeCourseInput {
    long courseId;
    String userId;
    long takeCourseId;
}
