package mission.fastlmsmission.member.history.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mission.fastlmsmission.common.model.LoginInput;
import mission.fastlmsmission.course.model.ServiceResult;
import mission.fastlmsmission.member.history.dto.HistoryDto;
import mission.fastlmsmission.member.history.entity.History;
import mission.fastlmsmission.member.history.service.HistoryService;
import mission.fastlmsmission.member.service.MemberService;
import mission.fastlmsmission.model.MemberInput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/history")
@Slf4j
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/save")
    public String historySave(HttpServletRequest request,
                              Model model, LoginInput parameter) {

        String userId = parameter.getUsername();
        System.out.println("userId = " + userId);
        String ip = request.getRemoteAddr();
        System.out.println("ip = " + ip);
        String userAgent = request.getHeader("user-agent");
        System.out.println("userAgent = " + userAgent);
        ServiceResult result = historyService.saveHistory(userId, ip, userAgent);

        if (!result.isResult()) {
            model.addAttribute("errorMessage", result.getMessage());
            return "error/error";
        }

        return "redirect:/";
    }
}
