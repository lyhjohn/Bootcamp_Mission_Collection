package zerobase.stock;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 스크래핑 1. 스크래핑 하려는 사이트에서 루트경로/robots.txt 를 통해 해당 페이지가 disallow에 등록돼있진 않은지 확인 2. 개발자도구-Elements에서
 * Jsoup.connect로 원하는 url의 html데이터 가져와 시작
 */
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class StockApplication {

	public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);

//        Scraper scraper = new YahooFinanceScraper();
////        Scraper scraper = new NaverFinanceScraper();
//        var result = scraper.scrap(Company.builder().ticker("MMM").build());
////        var result = scraper.scrapCompanyByTicker("MMM");
//        System.out.println("result = " + result);

//        try {
//            // 3. html 데이터 요청할 url 입력
//            Connection connect = Jsoup.connect("https://finance.yahoo.com/quote/KO/history?period1=-252374400&period2=1666310400&interval=1mo&filter=history&frequency=1mo&includeAdjustedClose=true");
//            // 4. Execute the request as a GET, and parse the result.
//            Document document = connect.get();
//            // 스크래핑 하는 html table 영역의 이름(data-test="historical-prices" 가져옴, 하나가 아닐 수 있으므로 Elements)
//            Elements eles = document.getElementsByAttributeValue("data-test", "historical-prices");
//            Element ele = eles.get(0);
//            /* 5. ele는 위에서 가져온 <table class="W(100%) M(0)" data-test="historical-prices"> 로써
//             해당 테이블 내에 html 태그들이 들어있음 */
//
//            // 6. 해당 영역의 큼지막한 태그들을 가져옴 get(0): thead / get(1): tbody / get(2): tfoot
//            Element tbody = ele.children().get(1); // 어떻게보면 순서가 정해진 table이기에 가능한 방법
//            for (Element e : tbody.children()) { // tbody 하위 태그들로 for문 돌림
//                String txt = e.text();
////                System.out.println("txt = " + txt);
//                if (!txt.endsWith("Dividend")) { // txt를 보면 해당 사이트에서는 배당금이 모두 Dividend로 끝남
//                    continue;
//                }
////                System.out.println(txt);
//                // 7. for문으로 뽑아낸 txt 형태를 보며 자르면 됨
//                String[] split = txt.split(" ");
//                String month = split[0];
//                int day = Integer.parseInt(split[1].replaceAll(",", ""));
//                int year = Integer.parseInt(split[2]);
//                String dividend = split[3];
//                System.out.println(year + "/" + month + "/" + day + " -> " + dividend);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
	}
}
