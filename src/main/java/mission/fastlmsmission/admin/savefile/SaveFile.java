package mission.fastlmsmission.admin.savefile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static java.time.LocalDateTime.*;

@Slf4j
public class SaveFile {

    public static String[] saveFilename(MultipartFile file) {



        if (file.getSize() > 0 && file.getOriginalFilename().length() > 0) {
            String newLocalFilename = getLocalFilename();
            String newUrlFilename = getUrlFilename();

            String uuid = UUID.randomUUID().toString().replaceAll("-", "");

            newLocalFilename += uuid + "." + getExtension(file.getOriginalFilename());
            newUrlFilename += uuid + "." + getExtension(file.getOriginalFilename());

            try {
                File saveFile = new File(newLocalFilename);
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(saveFile));
                newUrlFilename = "/" + newUrlFilename;
            } catch (IOException e) {
                log.info("File Coby Error");
                log.info(e.getMessage());
                return null;
            }


            return new String[]{newLocalFilename, newUrlFilename};
        }

        return new String[]{"empty"};
    }

    /**
     * C:\\Users\\82108\\Desktop\\갓영한\\Spring MVC 2\\mvc2_source_v20210624 (1)\\mvc2\\fastlmsMission\\files\\
     */
    public static String getLocalFilename() {

        String baseLocalPath = "C:/Users/82108/Desktop/갓영한/Spring MVC 2/mvc2_source_v20210624 (1)/mvc2/fastlmsMission/files/banner";
        String localDir = String.format("%s/%d/%02d/%02d/",
                baseLocalPath, now().getYear(), now().getMonthValue(), now().getDayOfMonth());

        // localDir 경로에 해당하는 폴더가 없으면 폴더를 생성한다 (상위폴더까지 탐색 후 한번에 생성).
        File file = new File(localDir);
        if (!file.isDirectory()) {
            file.mkdirs();
        }

        return localDir;
    }

    /**
     * /files/2022/10/06/5e6063dde1894462b6d4cc426ceccb80.png
     */
    public static String getUrlFilename() {

        String baseUrlPath = "files/banner"; // 앞에 "/(슬러시)"가 붙으면 경로에 파일 저장이 안됨
        String urlDir = String.format("%s/%d/%02d/%02d/",
                baseUrlPath, now().getYear(), now().getMonthValue(), now().getDayOfMonth());

        File file = new File(urlDir);
        if (!file.isDirectory()) {
            file.mkdirs();
        }

        return urlDir;
    }

    /**
     * 파일 확장자
     */
    public static String getExtension(String originalFilename) {
        if (originalFilename != null) {
            int dotPos = originalFilename.lastIndexOf(".");
            if (dotPos > -1) {
                return originalFilename.substring(dotPos + 1);
            }
        }
        return null;
    }


}
