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
@Data
public class Category {

    @Id
    @GeneratedValue
    Long id;

    String categoryName;
    int sortValue; //순서
    boolean usingYn;
}
