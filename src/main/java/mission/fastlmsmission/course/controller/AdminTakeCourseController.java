package mission.fastlmsmission.course.controller;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.course.dto.CourseDto;
import mission.fastlmsmission.course.dto.TakeCourseDto;
import mission.fastlmsmission.course.model.ServiceResult;
import mission.fastlmsmission.course.model.TakeCourseParam;
import mission.fastlmsmission.course.service.CourseService;
import mission.fastlmsmission.course.service.TakeCourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminTakeCourseController extends BaseController {

    private final TakeCourseService takeCourseService;
    private final CourseService courseService;

    @GetMapping("/takecourse/list.do")
    public String list(Model model, TakeCourseParam parameter, BindingResult bindingResult) {

        // binding 에러를 다루는 BindingResult를 변수로 받아서 BindingResult 에러 해결.
        // selectbox에서 default를 선택 시 id가 없어서 parameter.id와 mismatching 에러 발생하기 때문.

        parameter.init();

        List<TakeCourseDto> courses = takeCourseService.list(parameter); // totalCount 구해서 MemberDto에 넣는다.

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


        List<CourseDto> courseList = courseService.listAll();
        model.addAttribute("courseList", courseList);

        return "admin/takecourse/list";
    }

    @PostMapping("/takecourse/status.do")
    public String status(Model model, TakeCourseParam parameter) {

        ServiceResult result = takeCourseService.updateStatus(parameter.getId(), parameter.getStatus());
        if (!result.isResult()) {
            model.addAttribute("error", result.getMessage());
            return "error/admin_error";
        }

        return "redirect:/admin/takecourse/list.do";
    }

}
