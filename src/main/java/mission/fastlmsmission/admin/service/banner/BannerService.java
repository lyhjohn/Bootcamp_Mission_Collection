package mission.fastlmsmission.admin.service.banner;

import mission.fastlmsmission.admin.dto.banner.BannerDto;
import mission.fastlmsmission.admin.entity.Banner;

import java.util.List;

public interface BannerService {
    List<BannerDto> bannerList();
}
