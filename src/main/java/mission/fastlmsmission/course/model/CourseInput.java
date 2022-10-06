package mission.fastlmsmission.course.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseInput {
    private Long id;
    private String imagePath;
    private String keyword;
    private String subject;

    private String summary;

    private String contents;
    private Long price;
    private Long salePrice;
    private String saleEndDt;
    private LocalDateTime regDt;
    private LocalDateTime udtDt; // 업데이트 날짜

    private Long categoryId;

    private String idList;

    private String saveFileName;
    private String urlFileName;


}
