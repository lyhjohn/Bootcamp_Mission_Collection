package mission.fastlmsmission.admin.dto.banner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mission.fastlmsmission.admin.entity.Banner;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerDto {
    private Long id;

    private String baseLocalPath;
    private String baseUrlPath;

    public static BannerDto of(Banner banner) {
        return BannerDto.builder()
                .id(banner.getId())
                .baseLocalPath(banner.getBaseLocalPath())
                .baseUrlPath(banner.getBaseUrlPath())
                .build();
    }

    public static List<BannerDto> of(List<Banner> bannerList) {
        if (bannerList.isEmpty()) {
            return null;
        }

        List<BannerDto> list = new ArrayList<>();

        bannerList.forEach(banner -> list.add(BannerDto.of(banner)));

        return list;
    }
}
