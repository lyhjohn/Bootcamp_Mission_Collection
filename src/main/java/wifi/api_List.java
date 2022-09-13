package wifi;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.wifiDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * String으로 된 api 데이터를 json으로 변환 후, 각 필드값에 따른 객체들만 뽑아서 String 타입으로 dto에 저장한다.
 */
public class api_List {

    static int totalCount = 0;
    public List<wifiDto> stringToDto(String result) throws IOException {

        List<wifiDto> dtoList = new ArrayList<>();

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(result);

        JsonObject name = jsonObject.getAsJsonObject("TbPublicWifiInfo");

        JsonArray jsonArray = name.getAsJsonArray("row");
        totalCount = Integer.parseInt(name.getAsJsonPrimitive("list_total_count").toString());

        System.out.println("stringToDto실행");

        for (JsonElement json: jsonArray) {
            dtoList.add(new wifiDto(json.getAsJsonObject()));
        }

        return dtoList;
    }



//    public JsonArray getRows(String url) {
//        JsonObject jsonObject = JsonParser.parseString(url).getAsJsonObject();
//        JsonObject result = jsonObject.getAsJsonObject("TbPublicWifiInfo");
//
//        return result.get("row").getAsJsonArray();
//    }
//
//    public int getCount(String url) {
//        int count = JsonParser.parseString(url).getAsJsonObject()
//                .get("TbPublicWifiInfo")
//                .getAsJsonObject()
//                .get("list_total_count")
//                .getAsInt();
//
//        System.out.println(count);
//
//        return count;
//    }
}
