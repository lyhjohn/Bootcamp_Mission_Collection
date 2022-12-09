package zerobase.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * value: api doc에 표시되는 요청주소 옆에 표시되는 한줄 설명
     * notes: 파라미터값 보이기 전에 표시되는 노트
     */
    @ApiOperation(value = "일기 텍스트와 날씨 정보를 DB에 저장합니다.", notes = "이것은 노트")
    @PostMapping("/create/diary")
    public void createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                            @RequestBody String text) {
        diaryService.createDiary(date, text);
    }

    @ApiOperation("선택한 날짜에 작성된 모든 일기 데이터를 가져옵니다.")
    @GetMapping("/read/diary")
    List<Diary> readDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return diaryService.readDiary(date);
    }

    @ApiOperation("선택한 기간동안 작성한 모든 일기 데이터를 가져옵니다.")
    @GetMapping("/read/diaries")
    List<Diary> readDiaries(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                            @ApiParam(value = "조회할 기간의 첫 번째 날", example = "2022-10-14") LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                            @ApiParam(value = "조회할 기간의 마지막 날", example = "2022-10-14") LocalDate endDate) {

        // ApiParam: ApiDoc에 어떤 타입으로 날짜를 입력해야되는지 표시해줌

        return diaryService.readDiaries(startDate, endDate);
    }

    @PutMapping("/update/diary")
    Diary updateDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                      @RequestBody String text) {
        return diaryService.updateDiary(date, text);
    }

    @DeleteMapping("/delete/diary")
    String deleteDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        diaryService.deleteDiary(date);
        return "삭제완료";
    }
}
