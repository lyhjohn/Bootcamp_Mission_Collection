package mission.fastlmsmission.admin.mapper;

import mission.fastlmsmission.admin.dto.MemberDto;
import mission.fastlmsmission.admin.model.MemberParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    List<MemberDto> selectList(MemberParam parameter); // MemberMapper.xml에 등록한 id를 메서드 이름으로 사용함
    long selectListCount(MemberParam parameter);
}
