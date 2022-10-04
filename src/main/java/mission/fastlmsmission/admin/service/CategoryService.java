package mission.fastlmsmission.admin.service;

import mission.fastlmsmission.admin.dto.CategoryDto;
import mission.fastlmsmission.admin.model.CategoryInput;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> list();
    boolean add(String categoryName);
    boolean update(CategoryInput parameter);
    boolean delete(long id);

    // 프론트 카테고리 정보
    List<CategoryDto> frontList(CategoryDto parameter);
}
