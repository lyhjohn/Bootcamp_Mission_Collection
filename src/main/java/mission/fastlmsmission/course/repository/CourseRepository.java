package mission.fastlmsmission.course.repository;

import mission.fastlmsmission.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findBySubject(String subject);

    Optional<List<Course>> findByCategoryId(long categoryId);
}
