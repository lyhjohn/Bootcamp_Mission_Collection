package mission.fastlmsmission.admin.entity;

import lombok.*;
import mission.fastlmsmission.admin.model.banner.BannerInput;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Banner {

    @Id
    @GeneratedValue
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


    private String open;


    public static Banner createBanner(BannerInput param, String[] fileNames) {
        return Banner.builder()
                .id(param.getId())
                .url(param.getUrl())
                .localFilename(fileNames[0])
                .urlFilename(fileNames[1])
                .name(param.getName())
                .regDt(param.getRegDt())
                .open(param.getOpen())
                .openPublicYn(param.isOpenPublicYn())
                .seq(param.getSeq())
                .regDt(LocalDateTime.now())
                .build();
    }

    public void updateBanner(BannerInput parameter, String[] fileNames) {


        if (!fileNames[0].equals("empty")) {
            this.name = parameter.getName();
            this.localFilename = fileNames[0];
            this.urlFilename = fileNames[1];
            this.regDt = parameter.getRegDt();
            this.open = parameter.getOpen();
            this.openPublicYn = parameter.isOpenPublicYn();
            this.seq = parameter.getSeq();
            this.url = parameter.getUrl();
        } else {
            this.name = parameter.getName();
            this.regDt = parameter.getRegDt();
            this.open = parameter.getOpen();
            this.openPublicYn = parameter.isOpenPublicYn();
            this.seq = parameter.getSeq();
            this.url = parameter.getUrl();
        }
    }
}
