package mission.fastlmsmission.course.repository.service;

import mission.fastlmsmission.course.dto.CourseDto;
import mission.fastlmsmission.course.model.CourseInput;
import mission.fastlmsmission.course.model.CourseParam;

import java.util.List;

public interface CourseService {
    boolean add(CourseInput parameter);

    List<CourseDto> list(CourseParam parameter);

    CourseDto getById(Long id);

    boolean update(CourseInput parameter);

}
