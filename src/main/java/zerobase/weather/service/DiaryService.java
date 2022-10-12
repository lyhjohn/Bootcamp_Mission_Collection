package zerobase.weather.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DiaryService {


//     스프링부트에 지정된 openweathermap 변수로부터 키값을 가져와서 apiKey 객체에 넣어줌
    @Value("${openweathermap.key}") // key는 환경변수를 저장하는 yml 혹은 properties 파일에 저장
    private String apiKey;

    public void createDiary(LocalDate date, String text) {
        getWeatherString();
    }

    private String getWeatherString() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;
        return null;
    }
}
