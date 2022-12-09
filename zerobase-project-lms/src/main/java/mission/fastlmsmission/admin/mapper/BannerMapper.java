package mission.fastlmsmission.admin.mapper;

import mission.fastlmsmission.admin.dto.banner.BannerDto;
import mission.fastlmsmission.admin.dto.member.MemberDto;
import mission.fastlmsmission.admin.model.banner.BannerParam;
import mission.fastlmsmission.admin.model.member.MemberParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BannerMapper {
    long selectListCount(BannerParam parameter);
}
