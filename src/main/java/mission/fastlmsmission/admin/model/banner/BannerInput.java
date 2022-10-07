package mission.fastlmsmission.admin.model.banner;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BannerInput {
    private Long id;

    // C:\\Users\\82108\\Desktop\\갓영한\\Spring MVC 2\\mvc2_source_v20210624 (1)\\mvc2\\fastlmsMission\\files\\
    private String localFilename;

    // /files/2022/10/06/5e6063dde1894462b6d4cc426ceccb80.png
    private String urlFilename;
    private String url;


    private String name;
    private LocalDateTime regDt;
    private boolean openPublicYn;
    private long seq;

    private String openPos;
}
