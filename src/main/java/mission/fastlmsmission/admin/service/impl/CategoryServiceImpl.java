package mission.fastlmsmission.admin.service;

import mission.fastlmsmission.admin.dto.CategoryDto;

public interface CategoryService {
    boolean add(String categoryName);
    boolean update(CategoryDto parameter);
    boolean delete(long id);
}
