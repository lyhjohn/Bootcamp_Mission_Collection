package mission.fastlmsmission.admin.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class
AdminMainController {

    @GetMapping("/main.do")
    public String main() {
        return "admin/main";
    }
}
