package com.example.mission1.wifi.service;


import com.example.mission1.wifi.dto.WifiDto;
import com.example.mission1.wifi.ApiConnect;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class WifiService {
    public List<WifiDto> wifiSelect(String lat, String lnt) throws ClassNotFoundException, SQLException {

        List<WifiDto> wifiList = new ArrayList<>();

        String url = "jdbc:mariadb://localhost/wifi";
        String dbUserId = "WIFI_user";
        String dbPassword = "yohan";

        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;

        Class.forName("org.mariadb.jdbc.Driver");

        try {
            connection = DriverManager.getConnection(url, dbUserId, dbPassword);

            String sql = " select "
                    + " round((6371 * acos(cos(CAST(LNT AS FLOAT) * 3.141592653589793 / 180.0) * cos( " + lat + " * 3.141592653589793 / 180.0) "
                    + " * cos(( " + lnt + " * 3.141592653589793 / 180.0) - (CAST(LAT AS FLOAT) * 3.141592653589793 / 180.0)) "
                    + " + sin(CAST(LNT AS FLOAT) * 3.141592653589793 / 180.0) "
                    + " * sin( " + lat + " * 3.141592653589793 / 180.0))), 4) as distance, X_SWIFI_MGR_NO, X_SWIFI_WRDOFC, X_SWIFI_MAIN_NM, "
                    + " X_SWIFI_ADRES1, X_SWIFI_ADRES2, X_SWIFI_INSTL_FLOOR, X_SWIFI_INSTL_TY, X_SWIFI_INSTL_MBY,X_SWIFI_SVC_SE, "
                    + " X_SWIFI_CMCWR, X_SWIFI_CNSTC_YEAR, X_SWIFI_INOUT_DOOR, X_SWIFI_REMARS3, LAT, LNT, WORK_DTTM "
                    + " from wifi_info "
                    + " having distance < 10 "
                    + " order by distance "
                    + " limit 20; ";


            preparedStatement = connection.prepareStatement(sql);

            rs = preparedStatement.executeQuery();

            HistoryService history = new HistoryService();

            while (rs.next()) {
                WifiDto dto = new WifiDto();
                dto.setDistance(rs.getString("distance"));
                dto.setX_SWIFI_MGR_NO(rs.getString("X_SWIFI_MGR_NO"));
                dto.setX_SWIFI_WRDOFC(rs.getString("X_SWIFI_WRDOFC"));
                dto.setX_SWIFI_MAIN_NM(rs.getString("X_SWIFI_MAIN_NM"));
                dto.setX_SWIFI_ADRES1(rs.getString("X_SWIFI_ADRES1"));
                dto.setX_SWIFI_ADRES2(rs.getString("X_SWIFI_ADRES2"));
                dto.setX_SWIFI_INSTL_FLOOR(rs.getString("X_SWIFI_INSTL_FLOOR"));
                dto.setX_SWIFI_INSTL_TY(rs.getString("X_SWIFI_INSTL_TY"));
                dto.setX_SWIFI_INSTL_MBY(rs.getString("X_SWIFI_INSTL_MBY"));
                dto.setX_SWIFI_SVC_SE(rs.getString("X_SWIFI_SVC_SE"));
                dto.setX_SWIFI_CMCWR(rs.getString("X_SWIFI_CMCWR"));
                dto.setX_SWIFI_CNSTC_YEAR(rs.getString("X_SWIFI_CNSTC_YEAR"));
                dto.setX_SWIFI_INOUT_DOOR(rs.getString("X_SWIFI_INOUT_DOOR"));
                dto.setX_SWIFI_REMARS3(rs.getString("LAT"));
                dto.setLAT(rs.getString("LAT"));
                dto.setLNT(rs.getString("LNT"));
                dto.setWORK_DTTM(rs.getString("WORK_DTTM"));

                wifiList.add(dto);
            }
            history.setHistoryList(wifiList);

        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }

            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }

            if (connection != null && !connection.isClosed()) {
                connection.close();
            }

        }
        return wifiList;
    }

    /**
     * API 데이터를 쿼리 테이블에 저장해준다.
     * totalCount를 반환
     */
    public int wifiInsert() throws ClassNotFoundException, SQLException {

        ApiConnect api_connect = new ApiConnect();


        String url = "jdbc:mariadb://localhost/wifi";
        String dbUserId = "WIFI_user";
        String dbPassword = "yohan";


        Connection connection;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;

        Class.forName("org.mariadb.jdbc.Driver");
        connection = DriverManager.getConnection(url, dbUserId, dbPassword);

        Object[] objects = new Object[2];

        try {
            for (int i = 0; i < 18; i++) {
                String result = api_connect.getApi(i); // API 데이터를 1000개씩 끊어서 가져오는 메서드

                objects = WifiDto.stringToDto(result);// API 데이터를 String -> JSON -> Dto로 변환하는 메서드
                List<WifiDto> dtoList = (List<WifiDto>) objects[0];


                String sql = " insert into wifi_info (X_SWIFI_MGR_NO, X_SWIFI_WRDOFC, X_SWIFI_MAIN_NM, X_SWIFI_ADRES1, X_SWIFI_ADRES2, X_SWIFI_INSTL_FLOOR, X_SWIFI_INSTL_TY," +
                        " X_SWIFI_INSTL_MBY, X_SWIFI_SVC_SE, X_SWIFI_CMCWR, X_SWIFI_CNSTC_YEAR, X_SWIFI_INOUT_DOOR, X_SWIFI_REMARS3, LAT, LNT, WORK_DTTM) " +
                        " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                        " on duplicate key update X_SWIFI_MGR_NO=X_SWIFI_MGR_NO, X_SWIFI_WRDOFC=X_SWIFI_WRDOFC, " +
                        " X_SWIFI_MAIN_NM=X_SWIFI_MAIN_NM, X_SWIFI_ADRES1=X_SWIFI_ADRES1, X_SWIFI_ADRES2=X_SWIFI_ADRES2, " +
                        " X_SWIFI_INSTL_FLOOR=X_SWIFI_INSTL_FLOOR, X_SWIFI_INSTL_TY=X_SWIFI_INSTL_TY, X_SWIFI_INSTL_MBY=X_SWIFI_INSTL_MBY, " +
                        " X_SWIFI_SVC_SE=X_SWIFI_SVC_SE, X_SWIFI_CMCWR=X_SWIFI_CMCWR, X_SWIFI_CNSTC_YEAR=X_SWIFI_CNSTC_YEAR, X_SWIFI_INOUT_DOOR=X_SWIFI_INOUT_DOOR, " +
                        " X_SWIFI_REMARS3=X_SWIFI_REMARS3, LAT=LAT, LNT=LNT, WORK_DTTM=WORK_DTTM ; ";

                for (WifiDto dto : dtoList) {

                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, dto.getX_SWIFI_MGR_NO());
                    preparedStatement.setString(2, dto.getX_SWIFI_WRDOFC());
                    preparedStatement.setString(3, dto.getX_SWIFI_MAIN_NM());
                    preparedStatement.setString(4, dto.getX_SWIFI_ADRES1());
                    preparedStatement.setString(5, dto.getX_SWIFI_ADRES2());
                    preparedStatement.setString(6, dto.getX_SWIFI_INSTL_FLOOR());
                    preparedStatement.setString(7, dto.getX_SWIFI_INSTL_TY());
                    preparedStatement.setString(8, dto.getX_SWIFI_INSTL_MBY());
                    preparedStatement.setString(9, dto.getX_SWIFI_SVC_SE());
                    preparedStatement.setString(10, dto.getX_SWIFI_CMCWR());
                    preparedStatement.setString(11, dto.getX_SWIFI_CNSTC_YEAR());
                    preparedStatement.setString(12, dto.getX_SWIFI_INOUT_DOOR());
                    preparedStatement.setString(13, dto.getX_SWIFI_REMARS3());
                    preparedStatement.setString(14, dto.getLAT());
                    preparedStatement.setString(15, dto.getLNT());
                    preparedStatement.setString(16, dto.getWORK_DTTM());
                    preparedStatement.addBatch();
                    preparedStatement.executeUpdate();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }

            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }

            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

        return (int) objects[1];
    }
}