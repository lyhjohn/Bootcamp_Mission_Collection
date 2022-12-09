package mission.fastlmsmission.course.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mission.fastlmsmission.admin.model.CommonParam;

@Data
public class CourseParam extends CommonParam {
    long id;
    long categoryId;
}
