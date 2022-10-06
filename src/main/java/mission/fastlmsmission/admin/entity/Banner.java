package mission.fastlmsmission.admin.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Banner {

    @Id
    @GeneratedValue
    private Long id;

    private String baseLocalPath;
    private String baseUrlPath;
}
