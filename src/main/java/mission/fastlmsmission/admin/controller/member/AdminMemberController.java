package mission.fastlmsmission.admin.controller.member;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.dto.member.MemberDto;
import mission.fastlmsmission.admin.model.member.MemberParam;
import mission.fastlmsmission.admin.model.member.MemberInput;
import mission.fastlmsmission.course.controller.BaseController;
import mission.fastlmsmission.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminMemberController extends BaseController {

    private final MemberService memberService;

    @GetMapping("/member/list.do")
    public String list(Model model, MemberParam parameter) {

        parameter.init();

        List<MemberDto> members = memberService.memberList(parameter); // totalCount 구해서 MemberDto에 넣는다.

        long totalCount = 0;
        if (members != null && members.size() > 0) {
            totalCount = members.get(0).getTotalCount(); // 위에서 구한 totalCount를 MemberDto에서 꺼낸다.
        }

        String queryString = parameter.getQueryString(); // 검색 후 페이지 이동해도 검색결과가 초기화되지 않도록 구현
        String pagerHtml = super.getPagerHtml(totalCount, parameter.getPageEnd(), parameter.getPageIndex(),
                queryString);

        model.addAttribute("pager", pagerHtml);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("members", members);


        return "admin/member/list";
    }

    // 파라미터 -> Member엔티티 -> MemberDto
    @GetMapping("/member/detail.do")
    public String detail(Model model, MemberParam parameter) {
        MemberDto member = memberService.detail(parameter.getEmail());
        model.addAttribute("member", member);
        return "admin/member/detail";
    }

    @PostMapping("/member/status.do")
    public String status(Model model, MemberInput parameter) {
        String userStatus = parameter.getUserStatus();
        String email = parameter.getEmail();

        MemberDto memberDto = memberService.updateStatus(userStatus, email);

        return "redirect:/admin/member/detail.do?email=" + memberDto.getEmail();
    }

    @PostMapping("/member/password.do")
    public String updatePassword(Model model, MemberInput parameter) {
        String email = parameter.getEmail();
        String password = parameter.getPassword();

        MemberDto memberDto = memberService.updatePassword(email, password);

        return "redirect:/admin/member/detail.do?email=" + memberDto.getEmail();
    }

}                          


