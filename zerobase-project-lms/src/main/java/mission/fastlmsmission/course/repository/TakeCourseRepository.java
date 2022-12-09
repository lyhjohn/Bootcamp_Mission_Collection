package mission.fastlmsmission.course.repository;

import mission.fastlmsmission.course.dto.CourseDto;
import mission.fastlmsmission.course.entity.Course;
import mission.fastlmsmission.course.entity.TakeCourse;
import mission.fastlmsmission.course.model.TakeCourseParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TakeCourseRepository extends JpaRepository<TakeCourse, Long> {

    long countByCourseIdAndUserIdAndStatusIn(long courseId, String userId, Collection<String> statusList);
}
