package mission.fastlmsmission.course.controller;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.course.dto.CourseDto;
import mission.fastlmsmission.course.model.CourseInput;
import mission.fastlmsmission.course.model.CourseParam;
import mission.fastlmsmission.course.repository.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminCourseController extends BaseController {

    private final CourseService courseService;

    @GetMapping("/course/list.do")
    public String list(Model model, CourseParam parameter) {


        parameter.init();

        List<CourseDto> courses = courseService.list(parameter); // totalCount 구해서 MemberDto에 넣는다.

        long totalCount = 0;
        if (!CollectionUtils.isEmpty(courses) || courses.size() > 0) {
            totalCount = courses.get(0).getTotalCount(); // 위에서 구한 totalCount를 MemberDto에서 꺼낸다.
        }

        String queryString = parameter.getQueryString(); // 검색 후 페이지 이동해도 검색결과가 초기화되지 않도록 구현
        String pagerHtml = super.getPagerHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(),
                queryString);

        model.addAttribute("courses", courses);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/course/list";
    }

    @GetMapping(value = {"/course/add.do", "/course/edit.do"})
    public String add(Model model, HttpServletRequest request, CourseInput courseInput) {

        boolean editMode = request.getRequestURI().contains("/edit.do");
        CourseDto course = new CourseDto();
        if (editMode) {
            Long id = courseInput.getId();
            course = courseService.getById(id);
            if (course == null) {
                model.addAttribute("error", "강의 정보가 존재하지 않습니다.");
                return "error/error";
            }
        }
        model.addAttribute("course", course);
        model.addAttribute("editMode", editMode);
        return "admin/course/add";
    }

    @PostMapping(value = {"/course/add.do", "/course/edit.do"})
    public String addSubmit(Model model, HttpServletRequest request, CourseInput parameter) {
        boolean editMode = request.getRequestURI().contains("/edit.do");
        if (editMode) {
            boolean updateResult = courseService.update(parameter);
            if (!updateResult) {
                model.addAttribute("error", "강의 정보가 존재하지 않습니다.");
                return "error/error";
            }
        } else {
            boolean addResult = courseService.add(parameter);
            if (!addResult) {
                model.addAttribute("error", "해당 강의가 이미 존재합니다.");
                return "error/error";
            }
        }

        return "redirect:/admin/course/list.do";
    }
}
