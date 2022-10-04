package mission.fastlmsmission.admin.model;

import lombok.Data;

@Data
public class CommonParam {
    String searchType;
    String searchValue;
    long pageIndex;
    long pageSize;


    /**
     * limit 0, 10 -> pageIndex: 1
     * limit 10, 10 -> pageIndex: 2
     * limit 20, 10 -> pageIndex: 3
     * limit 30, 10 -> pageIndex: 4
     */
    public long getPageStart() { // 페이지 인덱스를 선택했을 때 몇번 데이터부터 보여줄지 정한다. limit x, y에서 x에 해당
        init();
        return (pageIndex - 1) * pageSize;
    }


    public long getPageEnd() { // limit x, y에서 y 담당
        init();
        return pageSize;
    }

    public void init() {
        if (pageIndex < 1) { // 페이지 번호
            pageIndex = 1;
        }
        if (pageSize < 10) { // 한 페이지에 들어있는 데이터 수
            pageSize = 10;
        }
    }

    public String getQueryString() {
        init();
        StringBuilder sb = new StringBuilder();
        if (searchType != null && searchType.length() > 0) {
            sb.append(String.format("searchType=%s", searchType));
        }

        if (searchValue != null && searchValue.length() > 0) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("searchValue=%s", searchValue));
        }
        return sb.toString();
    }
}
