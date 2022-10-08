package mission.fastlmsmission.admin.service.banner;

import mission.fastlmsmission.admin.dto.banner.BannerDto;
import mission.fastlmsmission.admin.entity.Banner;
import mission.fastlmsmission.admin.model.banner.BannerInput;
import mission.fastlmsmission.admin.model.banner.BannerParam;
import mission.fastlmsmission.course.model.ServiceResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BannerService {


    List<BannerDto> bannerList(BannerParam parameter);

    ServiceResult add(BannerInput parameter, MultipartFile file);

    ServiceResult delete(BannerParam param);

    BannerDto findOne(BannerInput param);

    ServiceResult update(BannerInput parameter, MultipartFile file);
}
