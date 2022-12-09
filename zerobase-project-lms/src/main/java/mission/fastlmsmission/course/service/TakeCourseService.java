package mission.fastlmsmission.course.service;

import mission.fastlmsmission.course.dto.CourseDto;
import mission.fastlmsmission.course.dto.TakeCourseDto;
import mission.fastlmsmission.course.entity.TakeCourse;
import mission.fastlmsmission.course.model.CourseParam;
import mission.fastlmsmission.course.model.ServiceResult;
import mission.fastlmsmission.course.model.TakeCourseParam;

import java.util.List;

public interface TakeCourseService {
    // 회원 강의 목록 리스트
    List<TakeCourseDto> list(TakeCourseParam parameter);

    // 수강 상태 변경
    ServiceResult updateStatus(long id, String status);

    // 내 수강내역
    List<TakeCourseDto> myCourseList(String userId);

    // 수강 내역 단건 조회
    TakeCourseDto detail(long id);

    // 내 수강신청 취소
    ServiceResult cancel(long id);
}
