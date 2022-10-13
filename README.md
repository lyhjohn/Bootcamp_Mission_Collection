# 와이파이 정보 구하기 프로젝트

## 홈
wifiPrint.jsp

<hr>

## 구현 기능

### 내 위치 가져오기
wifiPrint.jsp -> wifi.js 통해서 위치 가져옵니다.

<hr>

### Open API 와이파이 정보 가져오기
1. wifiPrint.jsp -> loadWifi.jsp 이동
2. 컨트롤러 이동 없이 jsp에서 바로 WifiService 클래스의 wifiInsert 메서드 호출 -> DB Insert
3. Insert 쿼리는 'on duplicate key update' 를 통해 중복된 값 저장을 방지했습니다.
4. addBatch와 executeUpdate를 통해 대용량 데이터 처리 속도를 개선해보려했는데 잘 된 건지는 잘 모르겠습니다.

<hr>

### 근처 와이파이 정보 보기
1. wifiPrint.jsp -> wifi.js -> WifiController 이동
2. WifiService 클래스의 wifiSelect 메서드를 통해 값을 가져왔습니다.
3. 메서드가 끝나는 부분에서 조회한 값들을 History에 저장하는 부분 구현

<hr>

### 위치 히스토리 목록
1. wifiPrint.jsp -> WifiController -> history.jsp 이동
1. 와이파이 정보 보기 단계에서 저장한 History 리스트를 history.jsp에서 뿌렸습니다.
