package mission.fastlmsmission.admin.controller.category;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.dto.category.CategoryDto;
import mission.fastlmsmission.admin.model.category.CategoryInput;
import mission.fastlmsmission.admin.model.member.MemberParam;
import mission.fastlmsmission.admin.service.category.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category/list.do")
    public String list(Model model, MemberParam parameter) {
        List<CategoryDto> categories = categoryService.list();
        model.addAttribute("categories", categories);
        return "admin/category/list";
    }

    @PostMapping("/category/add.do")
    public String addCategory(Model model, CategoryInput parameter) {

        boolean result = categoryService.add(parameter.getCategoryName());


        return "redirect:/admin/category/list.do";
    }

    @PostMapping("/category/delete.do")
    public String deleteCategory(Model model, CategoryInput parameter) {

        boolean result = categoryService.delete(parameter.getId());


        return "redirect:/admin/category/list.do";
    }

    @PostMapping("/category/update.do")
    public String updateCategory(Model model, CategoryInput parameter) {

        boolean result = categoryService.update(parameter);


        return "redirect:/admin/category/list.do";
    }
}
