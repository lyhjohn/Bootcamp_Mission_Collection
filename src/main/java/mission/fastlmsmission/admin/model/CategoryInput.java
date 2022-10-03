package mission.fastlmsmission.admin.model;

import lombok.Data;

@Data
public class CategoryInput {
    Long id;
    String categoryName;
    int sortValue;
    boolean usingYn;

}
