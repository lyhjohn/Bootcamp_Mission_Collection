package zerobase.stock.scraper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import zerobase.stock.model.Company;
import zerobase.stock.model.Dividend;
import zerobase.stock.model.ScrapedResult;
import zerobase.stock.model.constants.Month;

@Component
public class YahooFinanceScraper implements Scraper {

	private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true";
	private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
	private static final long START_TIME = 86400; // 60초 * 60분 * 24시간 (1일)

	@Override
	public ScrapedResult scrap(Company company) {
		var scrapResult = new ScrapedResult();
		scrapResult.setCompany(company);
		try {
			long now =
				System.currentTimeMillis() / 1000; // 1970년 1월 1일부터 현재까지의 시간을 ms로 받은 걸 초 단위로 변환
			String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, now);
			System.out.println("url = " + url);
			// 3. html 데이터 요청할 url 입력
			Connection connect = Jsoup.connect(url);
			// 4. Execute the request as a GET, and parse the result.
			Document document = connect.get();
			// 스크래핑 하는 html table 영역의 이름(data-test="historical-prices" 가져옴, 하나가 아닐 수 있으므로 Elements)
			Elements parsingDivs = document.getElementsByAttributeValue("data-test",
				"historical-prices");

			Element tableEle = parsingDivs.get(0);
            /* 5. ele는 위에서 가져온 <table class="W(100%) M(0)" data-test="historical-prices"> 로써
             해당 테이블 내에 html 태그들이 들어있음 */

			// 6. 해당 영역의 큼지막한 태그들을 가져옴 get(0): thead / get(1): tbody / get(2): tfoot
			Element tbody = tableEle.children().get(1); // 어떻게보면 순서가 정해진 table이기에 가능한 방법

			List<Dividend> dividendList = new ArrayList<>();
			for (Element e : tbody.children()) { // tbody 하위 태그들로 for문 돌림
				String txt = e.text();
//                System.out.println("txt = " + txt);
				if (!txt.endsWith("Dividend")) { // txt를 보면 해당 사이트에서는 배당금이 모두 Dividend로 끝남
					continue;
				}
//                System.out.println(txt);
				// 7. for문으로 뽑아낸 txt 형태를 보며 자르면 됨
				String[] split = txt.split(" ");
				int month = Month.strToNumber(split[0]);
				int day = Integer.valueOf(split[1].replaceAll(",", ""));
				int year = Integer.valueOf(split[2]);
				String dividend = split[3];

				if (month < 0) {
					throw new RuntimeException("Unexpected Month enum Value -> " + split[0]);
				}
				dividendList.add(new Dividend(LocalDateTime.of(year, month, day, 0, 0), dividend));
			}
			scrapResult.setDividends(dividendList);
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
		return scrapResult;
	}

	@Override
	public Company scrapCompanyByTicker(String ticker) {
		String url = String.format(SUMMARY_URL, ticker, ticker);
		try {
			Document document = Jsoup.connect(url).get();
			Element titleEle = document.getElementsByTag("h1").get(0);
			String title = titleEle.text().split(" ")[0].trim();
			return new Company(ticker, title);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
