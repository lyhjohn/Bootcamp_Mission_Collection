package mission.fastlmsmission.course.controller;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.dto.CategoryDto;
import mission.fastlmsmission.admin.service.CategoryService;
import mission.fastlmsmission.course.dto.CourseDto;
import mission.fastlmsmission.course.model.CourseParam;
import mission.fastlmsmission.course.repository.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/course")
public class CourseController extends BaseController {

    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping
    public String course(Model model, CourseParam parameter) {

        List<CourseDto> list = courseService.frontList(parameter);
        model.addAttribute("list", list);

        int courseTotalCount = 0;
        List<CategoryDto> categoryList = categoryService.frontList(CategoryDto.builder().build());
        if (categoryList != null) {
            for (CategoryDto i : categoryList) {
                courseTotalCount += i.getCourseCount();
            }
        }

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("courseCount", courseTotalCount);

        return "course/index";
    }

    @GetMapping("/{id}")
    public String courseDetail(Model model, CourseParam parameter) {
        CourseDto detail = courseService.frontDetail(parameter.getId());
        model.addAttribute("detail", detail);

        return "course/detail";
    }
}
