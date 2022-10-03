package mission.fastlmsmission.admin.service.impl;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.dto.CategoryDto;
import mission.fastlmsmission.admin.entity.Category;
import mission.fastlmsmission.admin.exception.CategoryAlreadyExist;
import mission.fastlmsmission.admin.model.CategoryInput;
import mission.fastlmsmission.admin.repository.CategoryRepository;
import mission.fastlmsmission.admin.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public boolean add(String categoryName) {

        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);
        if (optionalCategory.isPresent()) {
            throw new CategoryAlreadyExist("해당 카테고리가 이미 존재합니다.");
        }


        Category category = Category.builder()
                .categoryName(categoryName)
                .usingYn(true)
                .sortValue(0)
                .build();

        categoryRepository.save(category);

        return true;
    }

    @Override
    @Transactional
    public boolean update(CategoryInput parameter) {

        Optional<Category> optionalCategory = categoryRepository.findById(parameter.getId());
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setCategoryName(parameter.getCategoryName());
            category.setId(parameter.getId());
            category.setSortValue(parameter.getSortValue());
            category.setUsingYn(parameter.isUsingYn());
             return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        categoryRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public List<CategoryDto> list() {

        List<Category> categories = categoryRepository.findAll();

        return CategoryDto.of(categories);

        //        if (!CollectionUtils.isEmpty(categories) && categories.size() != 0) {
//            categories.forEach(e -> {
//                categoryDtoList.add(CategoryDto.of(e));
//            });
//        }
    }
}
