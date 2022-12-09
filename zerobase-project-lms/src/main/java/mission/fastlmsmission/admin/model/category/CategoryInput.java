package mission.fastlmsmission.admin.model.category;

import lombok.Data;

@Data
public class CategoryInput {
    Long id;
    String categoryName;
    int sortValue;
    boolean usingYn;

}
