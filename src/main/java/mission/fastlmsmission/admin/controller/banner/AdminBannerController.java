package mission.fastlmsmission.admin.controller.banner;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.dto.banner.BannerDto;
import mission.fastlmsmission.admin.model.banner.BannerInput;
import mission.fastlmsmission.admin.model.banner.BannerParam;
import mission.fastlmsmission.admin.service.banner.BannerService;
import mission.fastlmsmission.course.exception.CourseException;
import mission.fastlmsmission.course.model.ServiceResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminBannerController {

    private final BannerService bannerService;

    @GetMapping("/banner/list.do")
    public String list(Model model) {

        List<BannerDto> bannerList = bannerService.bannerList();

        model.addAttribute("bannerList", bannerList);
        return "/admin/banner/list";
    }

//    @GetMapping("/banner/add.do")
//    public String addBanner(Model model) {
//
//        return "/admin/banner/add";
//    }


    @PostMapping("/banner/delete.do")
    public String deleteBanner(BannerParam parameter, Model model) {

        ServiceResult result = bannerService.delete(parameter);

        if (!result.isResult()) {
            model.addAttribute("error", result.getMessage());
            return "error/admin_error";
        }

        return "redirect:/admin/banner/list.do";
    }

    @GetMapping(value = {"/banner/detail.do", "/banner/add.do"})
    public String detailBanner(BannerInput parameter, Model model, HttpServletRequest request) {

        boolean editMode = request.getRequestURI().contains("/detail.do");

        if (editMode) {
            BannerDto banner = bannerService.findOne(parameter);
            if (banner == null) {
                throw new CourseException("선택한 배너 정보가 없습니다.");
            }
            model.addAttribute("banner", banner);
        }

        model.addAttribute("editMode", editMode);

        return "/admin/banner/detail";
    }

    @PostMapping(value = {"/banner/detail.do", "/banner/add.do"})
    public String updateBanner(BannerInput parameter, MultipartFile file, Model model, HttpServletRequest request) {

        boolean editMode = request.getRequestURI().contains("/detail.do");

        if (editMode) {
            ServiceResult result = bannerService.update(parameter, file);
            if (!result.isResult()) {
                model.addAttribute("error", result.getMessage());
                return "error/admin_error";
            }
        }

        if (!editMode) {
            ServiceResult result = bannerService.add(parameter, file);
            if (!result.isResult()) {
                model.addAttribute("error", result.getMessage());
                return "error/admin_error";
            }
        }
        return "redirect:/admin/banner/list.do";
    }
}
