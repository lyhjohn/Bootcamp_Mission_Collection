package mission.fastlmsmission.course.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mission.fastlmsmission.admin.dto.category.CategoryDto;
import mission.fastlmsmission.admin.service.category.CategoryService;
import mission.fastlmsmission.course.dto.CourseDto;
import mission.fastlmsmission.course.model.CourseInput;
import mission.fastlmsmission.course.model.CourseParam;
import mission.fastlmsmission.course.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDate.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminCourseController extends BaseController {
    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping("/course/list.do")
    public String list(Model model, CourseParam parameter) {


        parameter.init();

        List<CourseDto> courses = courseService.list(parameter); // totalCount 구해서 MemberDto에 넣는다.

        long totalCount = 0;
        if (!CollectionUtils.isEmpty(courses) || courses.size() > 0) {
            totalCount = courses.get(0).getTotalCount(); // 위에서 구한 totalCount를 MemberDto에서 꺼낸다.
        }

        String queryString = parameter.getQueryString(); // 검색 후 페이지 이동해도 검색결과가 초기화되지 않도록 구현
        String pagerHtml = super.getPagerHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(),
                queryString);

        model.addAttribute("courses", courses);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);
        System.out.println("courses=" + courses);

        return "admin/course/list";
    }

    @GetMapping(value = {"/course/add.do", "/course/edit.do"})
    public String add(Model model, HttpServletRequest request, CourseInput courseInput) {

        List<CategoryDto> categories = categoryService.list();

        boolean editMode = request.getRequestURI().contains("/edit.do");
        CourseDto course = new CourseDto();
        if (editMode) {
            Long id = courseInput.getId();
            course = courseService.getById(id);
            if (course == null) {
                model.addAttribute("error", "강의 정보가 존재하지 않습니다.");
                return "error/admin_error";
            }
        }
        model.addAttribute("course", course);
        model.addAttribute("editMode", editMode);
        model.addAttribute("categories", categories);
        return "admin/course/add";
    }

    /**
     * 서버로 보내진 파일 저장하는 메서드
     *
     * @param baseLocalPath 전체에서 파일 경로
     * @param baseUrlPath 프로젝트 내에서 파일 경로
     * @return 서버로 보내진 파일을 새로운 이름으로 저장한 new 파일
     */
    String[] getNewSaveFile(String baseLocalPath, String baseUrlPath, String originalFileName) {

        // mkdir()을 이용한 방식 펼쳐보기
        // 경로를 따라서 폴더 생성을 위해 한 폴더씩 늘려가며 배열에 담았음
//        String[] dirs = {
//                String.format("%s\\%d\\", basePath, now().getYear()),
//                String.format("%s\\%d\\%02d\\",
//                        basePath, now().getYear(), now().getMonthValue()),
//                String.format("%s\\%d\\%02d\\%02d\\",
//                        basePath, now().getYear(), now().getMonthValue(), now().getDayOfMonth())
//        };
//        for (String dir : dirs) {
//            File file = new File(dir);
//            if (!file.isDirectory()) { // 경로에 폴더가 있으면 true, 없으면 false
//                file.mkdir();  // 폴더 생성(상위 폴더가 없으면 생성되지않음)
//            }
//        }

        // mkdirs() : 상위 폴더가 없으면 상위폴더까지 한꺼번에 생성
        // mkdir() : 해당하는 상위 폴더가 없으면 생성 못함(위 주석코드 처럼 폴더한개씩 생성해야함)

        String dirs = String.format("%s\\%d\\%02d\\%02d\\",
                baseLocalPath, now().getYear(), now().getMonthValue(), now().getDayOfMonth());

        //경로 폴더 만들기
        File file = new File(dirs);
        if (!file.isDirectory()) { // 경로에 폴더가 없다면 false
            file.mkdirs(); // 경로대로 폴더들을 만든다.
        }
        // mkdirs() : 상위 폴더가 없으면 상위폴더까지 한꺼번에 생성
        // mkdir() : 해당하는 상위 폴더가 없으면 생성 못함(위 주석코드 처럼 폴더한개씩 생성해야함)

        String urlDir = String.format("%s\\%d\\%02d\\%02d\\", baseUrlPath, now().getYear(), now().getMonthValue(), now().getDayOfMonth());

        String fileExtension = ""; // originalFileName 으로부터 확장자를 받을 변수
        if (originalFileName != null) {
            int dotPos = originalFileName.lastIndexOf(".");
            if (dotPos > -1) { // lastIndes가 -1이면 없다는 뜻임.
                fileExtension = originalFileName.substring(dotPos + 1); // dot 뒤부터 끝까지 잘라옴
            }
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String newFileName = String.format("%s%s", dirs, uuid); // C:\\Users\\82108\\Desktop\\갓영한\\Spring MVC 2\\mvc2_source_v20210624 (1)\\mvc2\\fastlmsMission\\files\\
        String newUrlFileName = String.format("%s%s", urlDir, uuid); // /files/2022/10/06/5e6063dde1894462b6d4cc426ceccb80.png
        if (fileExtension.length() > 0) {
            newFileName += "." + fileExtension;
            newUrlFileName += "." + fileExtension;
        }

        return new String[]{newFileName, newUrlFileName};
    }

    @PostMapping(value = {"/course/add.do", "/course/edit.do"})
    public String addSubmit(Model model, HttpServletRequest request, CourseInput parameter,
                            MultipartFile file) { // 파일을 전송받기 위해서 MultipartFile을 변수로 받음

        String saveFileName = "";
        String urlFileName = "";

        if (file != null) {
            String originalFilename = file.getOriginalFilename(); // 서버로 넘어온 파일의 오리지날 네임
            // 업로드한 파일이 저장되길 원하는 경로 설정 (경로에서 터미널 열고 pwd 명령어로 저장 경로 확인 가능)
            String baseLocalPath = "C:\\Users\\82108\\Desktop\\갓영한\\Spring MVC 2\\mvc2_source_v20210624 (1)\\mvc2\\fastlmsMission\\files\\";
            String baseUrlPath = "files";

            String[] arrFileName = getNewSaveFile(baseLocalPath, baseUrlPath, originalFilename);

            // 터미널에서 조회 시 파일 경로 C:\\Users\\82108\\Desktop\\갓영한\\Spring MVC 2\\mvc2_source_v20210624 (1)\\mvc2\\fastlmsMission\\files\\
            saveFileName = arrFileName[0];

            // 프로젝트 내의 파일 경로 /files/2022/10/06/5e6063dde1894462b6d4cc426ceccb80.png
            urlFileName = arrFileName[1];
            // 둘 중 어느 경로를 사용하건 상관없이 사진 출력 가능


            try {
                File newFile = new File(saveFileName);
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(newFile)); // 서버로 보내진 파일을 받아서 newFile 에 복사
            } catch (IOException e) {
                log.info("########## - 1");
                log.info(e.getMessage());
            }
        }
        parameter.setSaveFileName(saveFileName);
        parameter.setUrlFileName(urlFileName);

        boolean editMode = request.getRequestURI().contains("/edit.do");
        if (editMode) {
            boolean updateResult = courseService.update(parameter);
            if (!updateResult) {
                model.addAttribute("error", "강의 정보가 존재하지 않습니다.");
                return "error/admin_error";
            }
        } else {
            boolean addResult = courseService.add(parameter);
            if (!addResult) {
                model.addAttribute("error", "해당 강의가 이미 존재합니다.");
                return "error/admin_error";
            }
        }

        return "redirect:/admin/course/list.do";
    }

    @PostMapping("/course/delete.do")
    public String delete(Model model, HttpServletRequest request, CourseInput parameter) {
        boolean result = courseService.del(parameter.getIdList());


        return "redirect:/admin/course/list.do";
    }
}
