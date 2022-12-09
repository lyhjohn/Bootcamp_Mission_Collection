package mission.fastlmsmission.admin.mapper;

import mission.fastlmsmission.course.model.CourseParam;
import mission.fastlmsmission.course.dto.CourseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<CourseDto> selectList(CourseParam parameter); // MemberMapper.xml에 등록한 id를 메서드 이름으로 사용함
    long selectListCount(CourseParam parameter);
}
