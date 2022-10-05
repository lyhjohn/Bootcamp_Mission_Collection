package mission.fastlmsmission.course.service;

import mission.fastlmsmission.course.dto.CourseDto;
import mission.fastlmsmission.course.model.CourseInput;
import mission.fastlmsmission.course.model.CourseParam;
import mission.fastlmsmission.course.model.ServiceResult;
import mission.fastlmsmission.course.model.TakeCourseInput;

import java.util.List;

public interface CourseService {
    boolean add(CourseInput parameter);

    // 관리자 강의 리스트
    List<CourseDto> list(CourseParam parameter);

    CourseDto getById(Long id);

    boolean update(CourseInput parameter);

    boolean del(String idList);

    // 회원 강의 리스트
    List<CourseDto> frontList(CourseParam parameter);

    CourseDto frontDetail(long id);

    // 수강신청 결과
    ServiceResult req(TakeCourseInput parameter);
}
