package mission.fastlmsmission.admin.mapper;

import mission.fastlmsmission.admin.dto.category.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<CategoryDto> selectCategory(CategoryDto parameter);
}
