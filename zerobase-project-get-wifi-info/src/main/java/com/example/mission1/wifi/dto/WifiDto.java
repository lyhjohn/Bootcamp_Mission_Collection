package com.example.mission1.wifi.dto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WifiDto {

    String distance;
    String X_SWIFI_MGR_NO;
    String X_SWIFI_WRDOFC;
    String X_SWIFI_MAIN_NM;
    String X_SWIFI_ADRES1;
    String X_SWIFI_ADRES2;
    String X_SWIFI_INSTL_FLOOR;
    String X_SWIFI_INSTL_TY;
    String X_SWIFI_INSTL_MBY;
    String X_SWIFI_SVC_SE;
    String X_SWIFI_CMCWR;
    String X_SWIFI_CNSTC_YEAR;
    String X_SWIFI_INOUT_DOOR;
    String X_SWIFI_REMARS3;
    String LAT;
    String LNT;
    String WORK_DTTM;


    // 생성자를 통해 Json 형태로 데이터를 받아서 저장
    public WifiDto(JsonObject jsonObject) throws IOException {


        this.X_SWIFI_MGR_NO = jsonObject.get("X_SWIFI_MGR_NO").getAsString();
        this.X_SWIFI_WRDOFC = jsonObject.get("X_SWIFI_WRDOFC").getAsString();
        this.X_SWIFI_MAIN_NM = jsonObject.get("X_SWIFI_MAIN_NM").getAsString();
        this.X_SWIFI_ADRES1 = jsonObject.get("X_SWIFI_ADRES1").getAsString();
        this.X_SWIFI_ADRES2 = jsonObject.get("X_SWIFI_ADRES2").getAsString();
        this.X_SWIFI_INSTL_FLOOR = jsonObject.get("X_SWIFI_INSTL_FLOOR").getAsString();
        this.X_SWIFI_INSTL_TY = jsonObject.get("X_SWIFI_INSTL_TY").getAsString();
        this.X_SWIFI_INSTL_MBY = jsonObject.get("X_SWIFI_INSTL_MBY").getAsString();
        this.X_SWIFI_SVC_SE = jsonObject.get("X_SWIFI_SVC_SE").getAsString();
        this.X_SWIFI_CMCWR = jsonObject.get("X_SWIFI_CMCWR").getAsString();
        this.X_SWIFI_CNSTC_YEAR = jsonObject.get("X_SWIFI_CNSTC_YEAR").getAsString();
        this.X_SWIFI_INOUT_DOOR = jsonObject.get("X_SWIFI_INOUT_DOOR").getAsString();
        this.X_SWIFI_REMARS3 = jsonObject.get("X_SWIFI_REMARS3").getAsString();
        this.LAT = jsonObject.get("LAT").getAsString();
        this.LNT = jsonObject.get("LNT").getAsString();
        this.WORK_DTTM = jsonObject.get("WORK_DTTM").getAsString();
    }


    public static Object[] stringToDto(String result) throws IOException {

        List<WifiDto> dtoList = new ArrayList<>();

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(result);

        JsonObject name = jsonObject.getAsJsonObject("TbPublicWifiInfo");

        JsonArray jsonArray = name.getAsJsonArray("row");
        int totalCount = Integer.parseInt(name.getAsJsonPrimitive("list_total_count").toString());

        for (JsonElement json : jsonArray) {
            dtoList.add(new WifiDto(json.getAsJsonObject()));
        }


        return new Object[]{dtoList, totalCount};
    }
}
