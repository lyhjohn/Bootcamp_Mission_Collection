package mission.fastlmsmission.admin.controller.banner;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.dto.banner.BannerDto;
import mission.fastlmsmission.admin.model.banner.BannerInput;
import mission.fastlmsmission.admin.model.banner.BannerParam;
import mission.fastlmsmission.admin.service.banner.BannerService;
import mission.fastlmsmission.course.controller.BaseController;
import mission.fastlmsmission.course.exception.CourseException;
import mission.fastlmsmission.course.model.ServiceResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminBannerController extends BaseController {

    private final BannerService bannerService;

    @GetMapping("/banner/list.do")
    public String list(Model model, BannerParam parameter) {

        parameter.init();

        List<BannerDto> bannerList = bannerService.bannerList(parameter);

        if (bannerList == null || bannerList.size() < 1) {
            return "/admin/banner/list";
        }

        long totalCount = bannerList.get(0).getTotalCount();

        String queryString = parameter.getQueryString(); // 검색 후 페이지 이동해도 검색결과가 초기화되지 않도록 구현
        String pagerHtml = super.getPagerHtml(totalCount, parameter.getPageEnd(), parameter.getPageIndex(),
                queryString);


        model.addAttribute("pager", pagerHtml);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("bannerList", bannerList);
        return "/admin/banner/list";
    }


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
