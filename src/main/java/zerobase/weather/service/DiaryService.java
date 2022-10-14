package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.WeatherApplication;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.InvalidDate;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;

/**
 *     logback-spring.xml 파일에 의해 logs 패키지에 파일로 로그가 기록됨
 *     gitignore 파일에 logs 패키지 내 로그파일 추가해줘야됨
  */

    private static final Logger logger = LoggerFactory.getLogger(WeatherApplication.class); // 로그 만들기

    //     스프링부트에 지정된 openweathermap 변수로부터 키값을 가져와서 apiKey 객체에 넣어줌
    @Value("${openweathermap.key}") // key는 환경변수를 저장하는 yml 혹은 properties 파일에 저장
    private String apiKey;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    // 해당 트랜잭션이 도는 동안 다른 트랜잭션은 수행 안함
    public void createDiary(LocalDate date, String text) {

        logger.info("started to create diary");

        // 날씨 데이터 가져오기 (API에서 가져오기 or 이미 저장된 데이터면 db에서 가져오기
        DateWeather dateWeather = getDateWeather(date);

        /**
         *  파싱된 데이터 + 일기 값 데이터 넣기
         *  과거의 날씨를 가져오는것은 유료 + api 여러번 호출하는것도 유료
         *  따라서 가져온 날씨데이터를 별도의 db에 저장해놓고 해당 날짜 데이터가 있으면 api호출 없이 꺼내옴
         */
        Diary nowDiary = new Diary();
        nowDiary.setDateWeather(dateWeather);
        nowDiary.setText(text);

        diaryRepository.save(nowDiary);
        logger.info("end to create diary");
    }

    private DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> dateWeatherListFromDB = dateWeatherRepository.findAllByDate(date);
        if (dateWeatherListFromDB.isEmpty()) {
            return getWeatherFromApi();
        }

        return dateWeatherListFromDB.get(0);
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
                // br.readLine()이 not null일 때 inputLine = br.readLint()
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

        // 중괄호 열닫이 잘못되거나 문자열이 잘못된 경우 등 예외 발생 가능성 있음
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

    @Transactional(readOnly = true)
    // insert, update, delete 등의 기능을 막아둠 -> 트랜잭션 동작 속도가 빨라짐
    public List<Diary> readDiary(LocalDate date) {
//        if (date.isAfter(LocalDate.ofYearDay(3050, 1))) {
//            throw new InvalidDate();
//        }
        return diaryRepository.findAllByDate(date);
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }


    public Diary updateDiary(LocalDate date, String text) {
        Diary findDiary = diaryRepository.getFirstByDate(date);
        findDiary.setText(text);

        return findDiary;
    }


    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }


    /**
     * 초, 분, 시간, 일, 월, day of the week(0~7)
     * 매일 새벽 1시마다 데이터를 가져와서 DB에 저장해줌
     * (cron = "0/5 * * * * *") ->  5초 간격으로 저장해줌
     */
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void saveWeatherDate() {
        dateWeatherRepository.save(getWeatherFromApi());
    }

    // 매일 한번 날씨데이터를 가져오는 메서드
    private DateWeather getWeatherFromApi() {
        // open weather map에서 날씨 데이터 가져오기
        String weatherData = getWeatherString();
        // 날씨 json 파싱
        Map<String, Object> parsedWeather = parseWeather(weatherData);

        // 그날의 날씨 db에 기록
        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now());
        dateWeather.setIcon(parsedWeather.get("icon").toString());
        dateWeather.setWeather(parsedWeather.get("main").toString());
        dateWeather.setTemperature((Double) parsedWeather.get("temp"));

        return dateWeather;
    }
}
