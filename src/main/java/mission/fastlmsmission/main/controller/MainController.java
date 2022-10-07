package mission.fastlmsmission.main.controller;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.dto.banner.BannerDto;
import mission.fastlmsmission.admin.service.banner.BannerService;
import mission.fastlmsmission.components.MailComponents;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MailComponents mailComponents;
    private final BannerService bannerService;

    @RequestMapping("/")
    public String index(Model model) {
        List<BannerDto> bannerList = bannerService.bannerList();

        model.addAttribute("bannerList", bannerList);

        return "index";
    }


    @RequestMapping("/error/denied")
    public String errorDenied() {

        return "error/denied";
    }

//    @GetMapping("/index")
//    public String home() {
//        return "redirect:/";
//    }




    // request : web -> server
    // response : server -> seb

    /**
     * // request : web -> server
     * // response : server -> seb
     */
//    @RequestMapping(value = "/hello3", method = GET)
//    public void hello3(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        response.setContentType("text/html;charset=UTF-8");
//
//        PrintWriter writer = response.getWriter();
//        String str = "<html>" +
//                "<head>" +
//                "<meta charset=\"UTF-8\">" +
//                "</head>" +
//                "<body>" +
//                "<div>hello spring!!</div>" +
//                "<p>피태그</p>" +
//                "<p>피태그2</p>" +
//                "</body>" +
//                "</html>";
//        writer.write(str);
//        writer.close();
//    }
}
