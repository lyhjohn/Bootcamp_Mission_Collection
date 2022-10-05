package mission.fastlmsmission.member.controller;

import lombok.RequiredArgsConstructor;

import mission.fastlmsmission.admin.dto.MemberDto;
import mission.fastlmsmission.course.dto.TakeCourseDto;
import mission.fastlmsmission.course.model.ServiceResult;
import mission.fastlmsmission.course.service.TakeCourseService;
import mission.fastlmsmission.member.service.MemberService;
import mission.fastlmsmission.model.MemberInput;
import mission.fastlmsmission.model.ResetPasswordInput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TakeCourseService takeCourseService;

    @GetMapping("/login")
    public String login() {

        return "/member/login";
    }

    @GetMapping("/register") // 보여지는 url
    public String register(Model model) {
        model.addAttribute("member", new MemberInput());
        return "/member/register"; // 보여지는 view
    }

    @PostMapping("/register")
    public String registerSubmit(MemberInput memberInput, Model model) {
        boolean result = memberService.register(memberInput);
        model.addAttribute("result", result);

        return "member/register_complete";
    }

    // 프로토콜://도메인(IP)/../..?파라미터=...
    @GetMapping("/email-auth")
    public String emailAuth(HttpServletRequest request, Model model) {
        String uuid = request.getParameter("id");
        boolean emailAuth = memberService.emailAuth(uuid);
        model.addAttribute("emailAuth", emailAuth);
        return "member/email_auth";
    }

    @GetMapping("/info")
    public String memberInfo(Principal principal, Model model) {
        //현재 로그인 정보에서 아이디를 가져옴
        String userId = principal.getName();

        MemberDto detail = memberService.detail(userId);
        model.addAttribute("detail", detail);
        return "member/info";
    }

    @PostMapping("/info")
    public String memberInfoSubmit(Principal principal, Model model, MemberInput parameter) {
        //현재 로그인 정보에서 아이디를 가져옴
        String userId = principal.getName();
        parameter.setEmail(userId);
        ServiceResult result = memberService.updateMember(parameter);
        if (!result.isResult()) {
            model.addAttribute("message", result.getMessage());
            return "error/error";
        }

        return "redirect:/member/info";
    }

    @GetMapping("/password")
    public String memberPassword() {

        return "/member/password";
    }

    @PostMapping("/password")
    public String memberPasswordSubmit(Model model, MemberInput parameter, Principal principal) {
        //현재 로그인 정보에서 아이디를 가져옴
        String userId = principal.getName();

        parameter.setEmail(userId);

        ServiceResult result = memberService.updateMemberPassword(parameter);
        if (!result.isResult()) {
            model.addAttribute("message", result.getMessage());
            return "error/error";
        }
        return "redirect:/member/info";
    }

    @GetMapping("/takecourse")
    public String memberTakeCourse(Principal principal, Model model) {
        String userId = principal.getName();

        List<TakeCourseDto> courses = takeCourseService.myCourseList(userId);

        model.addAttribute("courses", courses);

        return "member/takecourse";
    }

    @GetMapping("/find-password")
    public String findPassword() {
        return "member/find_password";
    }

    @PostMapping("/find-password")
    public String findPassword(ResetPasswordInput parameter,
                               Model model) {
        boolean result = false;
        try {
            result = memberService.sendResetPassword(parameter);
        } catch (Exception e) {

        }
        model.addAttribute("result", result);
        return "member/find_password_result";
    }

    @GetMapping("/reset/password")
    public String resetPassword(HttpServletRequest request, Model model) {
        String uuid = request.getParameter("id");

        // 비밀번호가 변경되었다면 Member의 resetPasswordKey(uuid) = null
        boolean result = memberService.checkValidUuid(uuid);

        model.addAttribute("result", result);

        return "member/reset_password";
    }

    @PostMapping("/reset/password")
    public String resetPassword(ResetPasswordInput parameter, Model model) {
        // url에 포함된 파라미터값(id)도 객체의 속성에 매핑된다.
        boolean result = false;
        try {
            result = memberService.resetPassword(parameter.getId(),
                    parameter.getPassword());
        } catch (Exception e) {

        }
        model.addAttribute("result", result);
        return "member/reset_password_result";
    }
}
