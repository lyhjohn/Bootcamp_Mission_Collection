package wifi;

import dto.historyDto;
import dto.wifiDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class saveHistory {

    static historyDto history_dto = new historyDto();

    /**
     * 위치 히스토리 목록 보기를 누르면 컨트롤러를 통해 historyDto에서 해당 데이터를 꺼내와서 history.jsp로 보낸다.
     */
    public List<historyDto> getHistory() {

        return history_dto.getHistoryList();
    }

    /**
     * wifiService에서 WIFI_Select 메서드가 실행될 떄 작동한다.
     * 근처 와이파이 정보를 가져오면서 historyDto에 저장해준다.
     */
    public void setHistoryList(List<wifiDto> historyList) {

        List<historyDto> saveHistoryList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        if (historyList != null) {
            int id = historyList.size();
            for (wifiDto dto : historyList) {
                history_dto = new historyDto();
                history_dto.setID(id--);
                history_dto.setX(dto.getLAT());
                history_dto.setY(dto.getLNT());
                history_dto.setRegisteredAt(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                saveHistoryList.add(history_dto);
            }
        }
        history_dto.setHistoryList(saveHistoryList);
    }
}
