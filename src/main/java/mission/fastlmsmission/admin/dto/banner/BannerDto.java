package mission.fastlmsmission.admin.dto.banner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mission.fastlmsmission.admin.entity.Banner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerDto {
    private Long id;

    // C:\\Users\\82108\\Desktop\\갓영한\\Spring MVC 2\\mvc2_source_v20210624 (1)\\mvc2\\fastlmsMission\\files\\
    private String localFilename;

    // /files/2022/10/06/5e6063dde1894462b6d4cc426ceccb80.png
    private String urlFilename;

    private String name;
    private LocalDateTime regDt;

    private String url;

    private boolean openPublicYn;
    private long seq;

    private String open;


    public static BannerDto of(Banner banner) {
        return BannerDto.builder()
                .id(banner.getId())
                .localFilename(banner.getLocalFilename())
                .urlFilename(banner.getUrlFilename())
                .name(banner.getName())
                .regDt(banner.getRegDt())
                .url(banner.getUrl())
                .open(banner.getOpen())
                .openPublicYn(banner.isOpenPublicYn())
                .seq(banner.getSeq())
                .build();
    }

    public static List<BannerDto> of(List<Banner> bannerList) {
        List<BannerDto> list = new ArrayList<>();

        bannerList.forEach(banner -> list.add(BannerDto.of(banner)));

        return list;
    }

    public String getRegDt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return this.regDt != null ? this.regDt.format(formatter) : "";
    }
}
