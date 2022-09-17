<%@ page import="java.util.List" %>
<%@ page import="com.example.mission1.dto.wifiDto" %>
<%@ page import="com.example.mission1.dto.historyDto" %>
<%@ page import="com.example.mission1.wifi.wifiService" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>와이파이 정보 구하기</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/history.css"/>

    <h1>위치 히스토리 목록</h1>

    <style>
        td, th {
            border: 1px solid;
        }

        table {
            border-collapse: collapse;
        }

    </style>
</head>
<body>
<a href="${pageContext.request.contextPath}/wifiPrint.jsp">홈</a>
|
<a href="${pageContext.request.contextPath}/WIFI/history">위치 히스토리 목록</a>
|
<a href="${pageContext.request.contextPath}/loadWifi.jsp">Open API 와이파이 정보 가져오기</a>


<table>
    <thead>
    <tr class="table-head">
        <th width="110px">ID</th>
        <th width="230px">X좌표</th>
        <th width="230px">Y좌표</th>
        <th width="400px">조회일자</th>
        <th width="140px">비고</th>
    </tr>
    </thead>
    <tbody>
    <tr>
            <%
            List<historyDto> historyList = (List<historyDto>) request.getAttribute("historyList");
            if (historyList != null) {
            for (historyDto dto : historyList) {
            %>
    </tr>
        <td><%=dto.getID()%>
        </td>
        <td><%=dto.getY()%>
        </td>
        <td><%=dto.getX()%>
        </td>
        <td><%=dto.getRegisteredAt()%>
        </td>
        <td>
            <button class="delete-btn">삭제</button>
        </td>
    <tr>
    <%
            }
        }
    %>
    </tr>
    </tbody>
</table>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/history.js">
</script>
</body>
</html>