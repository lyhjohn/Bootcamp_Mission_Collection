package mission.fastlmsmission.admin.mapper;

import mission.fastlmsmission.course.dto.TakeCourseDto;
import mission.fastlmsmission.course.model.TakeCourseParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TakeCourseMapper {
    List<TakeCourseDto> selectList(TakeCourseParam parameter); // MemberMapper.xml에 등록한 id를 메서드 이름으로 사용함
    long selectListCount(TakeCourseParam parameter);

    List<TakeCourseDto> selectListMyCourse(TakeCourseParam parameter);
}
