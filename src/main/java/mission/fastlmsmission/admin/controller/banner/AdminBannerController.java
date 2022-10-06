package mission.fastlmsmission.admin.controller.banner;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.dto.banner.BannerDto;
import mission.fastlmsmission.admin.service.banner.BannerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminBannerController {

    private  final BannerService bannerService;

    @GetMapping("/banner/add.do")
    public String addBanner(Model model) {

        List<BannerDto> bannerList = bannerService.bannerList();

        if (bannerList == null) {
            model.addAttribute("message", "배너가 존재하지 않습니다.");
            return "/admin/banner/add";
        }

        model.addAttribute("bannerList", bannerList);
        return "/admin/banner/add";
    }
}
