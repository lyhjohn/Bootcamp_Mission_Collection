package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    //     스프링부트에 지정된 openweathermap 변수로부터 키값을 가져와서 apiKey 객체에 넣어줌
    @Value("${openweathermap.key}") // key는 환경변수를 저장하는 yml 혹은 properties 파일에 저장
    private String apiKey;

    @Transactional
    public Diary createDiary(LocalDate date, String text) {

        // openweathermap api에서 날씨 데이터 가져오기
        String weatherData = getWeatherString();

        // 받아온 날씨 json 파싱하기
        Map<String, Object> parsedWeather = parseWeather(weatherData);

        // 파싱된 데이터 + 일기 값 데이터 넣기
        Diary nowDiary = new Diary();
        nowDiary.setWeather(parsedWeather.get("main").toString());
        nowDiary.setIcon(parsedWeather.get("icon").toString());
        nowDiary.setTemperature((Double) parsedWeather.get("temp"));
        nowDiary.setText(text);
        nowDiary.setDate(date);

        return diaryRepository.save(nowDiary);
    }

    private String getWeatherString() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;
        try {
            URL url = new URL(apiUrl); // String -> URL 타입 변경
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Http 형식으로 URL open
            connection.setRequestMethod("GET"); // api 주소에 GET 방식으로 요청을 보냄(api 쪽에서 GET으로 요청받아서 데이터 보내는것)
            int responseCode = connection.getResponseCode(); // 응답 코드 (200, 404 등)
            BufferedReader br; // 긴 응답 결과를 받을 때 효과적임
            if (responseCode == 200) { // InputStream으로 응답 객체 결과를 받아옴
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) { // br.readLine을 두번 -> 한번 호출로 줄이고자 변수에 할당
                response.append(inputLine);
            }
            br.close();
            return response.toString();

        } catch (Exception e) {
            return "failed to get response";
        }
    }

    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        // 중괄호 열닫히 잘못되거나 문자열이 잘못된 경우 등 예외 발생 가능성 있음
        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString); // String -> JsonObject
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashMap<>();

        JSONObject mainData = (JSONObject) jsonObject.get("main");
        JSONArray weatherData = (JSONArray) jsonObject.get("weather");

        resultMap.put("temp", mainData.get("temp"));
        resultMap.put("main", ((JSONObject) weatherData.get(0)).get("main"));
        resultMap.put("icon", ((JSONObject) weatherData.get(0)).get("icon"));

        return resultMap;
    }
}
