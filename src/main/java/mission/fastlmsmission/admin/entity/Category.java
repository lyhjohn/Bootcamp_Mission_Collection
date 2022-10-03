package mission.fastlmsmission.admin.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String categoryName;
    private int sortValue; //순서
    private boolean usingYn;
}
