package mission.fastlmsmission.course.controller;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.service.CategoryService;
import mission.fastlmsmission.common.model.ResponseResult;
import mission.fastlmsmission.course.model.ServiceResult;
import mission.fastlmsmission.course.model.TakeCourseInput;
import mission.fastlmsmission.course.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class ApiCourseController extends BaseController {

    private final CourseService courseService;
    private final CategoryService categoryService;

    @PostMapping("/api/course/req.api")
    public ResponseEntity<?> courseReq(Model model, @RequestBody TakeCourseInput parameter
            , Principal principal) { // 스프링 security에서 Principal에 로그인정보를 주입해줌

        parameter.setUserId(principal.getName()); // getName이 security에서 로그인한 유저 아이디가 됨
        ServiceResult result = courseService.req(parameter);
        if (!result.isResult()) {
            ResponseResult responseResult = new ResponseResult(false, result.getMessage());
            return ResponseEntity.badRequest().body(responseResult);
        }

        ResponseResult responseResult = new ResponseResult(true);
        return ResponseEntity.ok().body(responseResult);
    }
}
