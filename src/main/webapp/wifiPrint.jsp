
<%@ page import="com.example.mission1.wifi.dto.WifiDto" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>와이파이 정보 구하기</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/wifiPrint.css"/>
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

<h1>와이파이 정보 구하기</h1>
<a href="${pageContext.request.contextPath}/wifiPrint.jsp">홈</a>
|
<a href="${pageContext.request.contextPath}/WIFI/history">위치 히스토리 목록</a>
|
<a href="${pageContext.request.contextPath}/loadWifi.jsp">Open API 와이파이 정보 가져오기</a>


<br>
<div class="sub-nav">
	<div class="lat-input-wrap inline-element">
	<span> LAT </span>
	<input id="lat-input">
	</div>
	
	<div class="lnt-input-wrap inline-element">
	<span> LNT </span>
	<input id="lnt-input">
	</div>
	
	<div class="btn-group inline-element">
		<button id="get-position-btn">내 위치 가져오기</button>
		<button id="get-wifi-btn">근처 와이파이 정보 보기</button>
	</div>
</div>

<table>
    <thead>
    <tr class="table-head">
        <th height="50px" width="60px">거리 (Km)</th>
        <th width="100px">관리번호</th>
        <th width="40px">자치구</th>
        <th width="110px">와이파이명</th>
        <th width="120px">도로명주소</th>
        <th width="200px">상세주소</th>
        <th width="70px">설치위치(층)</th>
        <th width="110px">설치유형</th>
        <th width="60px">설치기관</th>
        <th width="60px">서비스구분</th>
        <th width="60px">망종류</th>
        <th width="50px">설치년도</th>
        <th width="50px">실내외구분</th>
        <th width="60px">WIFI접속환경</th>
        <th width="60px">X좌표</th>
        <th width="60px">Y좌표</th>
        <th width="120px">작업일자</th>
    </tr>
    </thead>
    <tbody>
    <tr>
            <%
            	List<WifiDto> wifiList = (List<WifiDto>) request.getAttribute("wifiList");
          		if(wifiList != null) {
                for (WifiDto dto: wifiList) {
            %>
    <tr>
        <td><%=dto.getDistance()%></td>
        <td><%=dto.getX_SWIFI_MGR_NO()%></td>
        <td><%=dto.getX_SWIFI_WRDOFC()%></td>
        <td><%=dto.getX_SWIFI_MAIN_NM()%></td>
        <td><%=dto.getX_SWIFI_ADRES1()%></td>
        <td><%=dto.getX_SWIFI_ADRES2()%></td>
        <td><%=dto.getX_SWIFI_INSTL_FLOOR()%></td>
        <td><%=dto.getX_SWIFI_INSTL_TY()%></td>
        <td><%=dto.getX_SWIFI_INSTL_MBY()%></td>
        <td><%=dto.getX_SWIFI_SVC_SE()%></td>
        <td><%=dto.getX_SWIFI_CMCWR()%></td>
        <td><%=dto.getX_SWIFI_CNSTC_YEAR()%></td>
        <td><%=dto.getX_SWIFI_INOUT_DOOR()%></td>
        <td><%=dto.getX_SWIFI_REMARS3()%></td>
        <td><%=dto.getLNT()%></td>
        <td><%=dto.getLAT()%></td>
        <td><%=dto.getWORK_DTTM()%></td>
    </tr>
    <%
        }
          		} else {
    %>
    <tr>
        <td colspan="17">
            <p class="array-center">위치 정보를 입력한 후에 조회해 주세요.</p>
        </td>
    </tr>

    <%
        }
    %>
    </tbody>
</table>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/wifi.js">

</script>
</body>
</html>