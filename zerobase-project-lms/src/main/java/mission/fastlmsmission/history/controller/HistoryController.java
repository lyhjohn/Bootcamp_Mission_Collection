package mission.fastlmsmission.history.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mission.fastlmsmission.common.model.LoginInput;
import mission.fastlmsmission.course.model.ServiceResult;
import mission.fastlmsmission.history.service.HistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/history")
@Slf4j
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/save")
    public String historySave(HttpServletRequest request,
                              Model model, LoginInput parameter) { // 로그인페이지에서 넘겨준 파라미터값

        String userId = parameter.getUsername();
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("user-agent");

        ServiceResult result = historyService.saveHistory(userId, ip, userAgent);

        if (!result.isResult()) {
            model.addAttribute("error", result.getMessage());
            return "error/admin_error";
        }

        return "redirect:/";
    }
}
