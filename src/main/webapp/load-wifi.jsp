<%@page import="WIFI.wifiService" %>
<%@ page import="java.sql.SQLException" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>와이파이 정보 구하기</title>
    <link rel="stylesheet"href="${pageContext.request.contextPath}/resources/css/wifiPrint.css"/>

</head>
<%--Open API 와이파이 정보를 DB에 insert하는 코드를 구현했습니다.
컨트롤러를 거치지 않고 바로 wifiService 클래스로 이동하여 insert 부분을 실행합니다. --%>
<body>
<%
    wifiService ws = new wifiService();
    int totalCount = 0;
    try {
        totalCount = ws.wifi_Insert();
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