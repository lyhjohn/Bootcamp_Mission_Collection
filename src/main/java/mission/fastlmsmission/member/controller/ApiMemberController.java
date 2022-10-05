package mission.fastlmsmission.member.controller;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.common.model.ResponseResult;
import mission.fastlmsmission.course.dto.TakeCourseDto;
import mission.fastlmsmission.course.model.ServiceResult;
import mission.fastlmsmission.course.model.TakeCourseInput;
import mission.fastlmsmission.course.service.TakeCourseService;
import mission.fastlmsmission.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ApiMemberController {

    private final MemberService memberService;
    private final TakeCourseService takeCourseService;

    @PostMapping("/api/member/course/cancel.api")
    public ResponseEntity<?> cancelCourse(@RequestBody TakeCourseInput parameter, Principal principal) {
        TakeCourseDto detail = takeCourseService.detail(parameter.getTakeCourseId());
        if (detail == null) {
            ResponseResult responseResult = new ResponseResult(false, "수강 신청 정보가 존재하지 않습니다.");
            return ResponseEntity.badRequest().body(responseResult);
        }

        if (!detail.getUserId().equals(principal.getName()) || principal.getName() == null) {
            ResponseResult responseResult = new ResponseResult(false, "본인의 수강신청 정보만 취소할 수 있습니다.");
            return ResponseEntity.badRequest().body(responseResult);
        }
        ServiceResult result = takeCourseService.cancel(parameter.getTakeCourseId());
        if (!result.isResult()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        return ResponseEntity.ok().body(true);

    }
}
