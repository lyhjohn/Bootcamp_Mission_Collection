package WIFI;

import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;



@NoArgsConstructor
public class apiConnect { // OpenAPI에 접근해서 String 객체로 데이터를 가져온다.

    public String getApi(int pageIdx) {

        String key = "6e6d6b784c646c6134356176786150";
        String result = "";

        int start = pageIdx * 1000 + 1;
        int end = start + 999;

        try {
            URL apiUrl = new URL("http://openapi.seoul.go.kr:8088/" + key
                    + "/json/TbPublicWifiInfo/" + start + "/" + end);

            BufferedReader br = new BufferedReader(new InputStreamReader(apiUrl.openStream(), StandardCharsets.UTF_8));

            result = br.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
