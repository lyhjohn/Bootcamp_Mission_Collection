<%@ page import="java.sql.SQLException" %>
<%@ page import="com.example.mission1.wifi.service.WifiService" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>와이파이 정보 구하기</title>
    <link rel="stylesheet"href="${pageContext.request.contextPath}/resources/css/wifiPrint.css"/>
</head>

<body>
<%
    WifiService ws = new WifiService();
    int totalCount = 0;
    try {
        totalCount = ws.wifiInsert();
    } catch (ClassNotFoundException | SQLException e) {
        throw new RuntimeException(e);
    }
%>

<div class="array-center">
    <h1><%=totalCount%>개의 WIFI 정보를 정상적으로 저장하였습니다.</h1>
    <a href="wifiPrint.jsp">홈으로 가기</a>
</div>
</body>
</html>