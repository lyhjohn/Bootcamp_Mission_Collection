package mission.fastlmsmission.course.model;

import lombok.Data;
import mission.fastlmsmission.admin.model.CommonParam;

@Data
public class TakeCourseParam extends CommonParam {
    long id;
    String status;
    String userId;
    long searchCourseId;
}
