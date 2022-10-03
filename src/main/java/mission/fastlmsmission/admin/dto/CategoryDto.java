package mission.fastlmsmission.admin.dto;

import lombok.*;
import mission.fastlmsmission.admin.entity.Category;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private Long id;
    private String categoryName;
    int sortValue; //순서
    boolean usingYn;


    public static CategoryDto of(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .sortValue(category.getSortValue())
                .usingYn(category.isUsingYn())
                .build();
    }

    public static List<CategoryDto> of(List<Category> categories) {
        if (categories != null) {
            List<CategoryDto> categoryDtoList = new ArrayList<>();
            for (Category c : categories) {
                categoryDtoList.add(of(c));
            }
            return categoryDtoList;
        }
        return null;
    }

}
