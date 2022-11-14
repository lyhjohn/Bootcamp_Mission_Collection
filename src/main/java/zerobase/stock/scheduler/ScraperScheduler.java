package zerobase.stock.scheduler;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Cache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zerobase.stock.model.Company;
import zerobase.stock.model.ScrapedResult;
import zerobase.stock.persist.CompanyRepository;
import zerobase.stock.persist.DividendRepository;
import zerobase.stock.persist.entity.CompanyEntity;
import zerobase.stock.persist.entity.DividendEntity;
import zerobase.stock.scraper.Scraper;

@Component
@Slf4j
@AllArgsConstructor
public class ScraperScheduler {

//	@Scheduled(cron = "0/5 * * * * *") // 초 분 시 일 월 요일 연도(생략가능)
//	public void test() {
//		System.out.println("now -> " + System.currentTimeMillis());
//	}

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;
	private final Scraper yahooFinanceScraper;

	// 스케줄러는 한개의 스레드로 동작함. 따라서 여러개의 스케줄러가 존재할 경우에는
	// 하나의 스레드로 나눠가면서 번갈아서 실행됨.
	// 그러므로 스케줄 주기가 다르더라도 한번씩 실행되므로 원하는 주기대로 돌릴 수가 없음
	// 스레드 풀로 스케줄을 여러개 만들어서 이 부분을 해결할 수 있음 -> SchedulerConfig 파일에 있음

	// 일정 주기마다 수행
//	@Scheduled(cron = "${scheduler.scrap.yahoo}") // 매일 정각
	public void yahooFinanceScheduling() {
		log.info("scraping scheduler is started");
		//저장된 회사 목록 조회
		List<CompanyEntity> companies = companyRepository.findAll();

		//회사마다 배당금 정보 새로 스크래핑
		for (CompanyEntity company : companies) {
			log.info("scraping scheduler is started -> {}", company.getName());
			ScrapedResult scrapedResult = yahooFinanceScraper.scrap(
				new Company(company.getName(), company.getTicker()));

			//스크래핑한 배당금 정보 중 DB에 없는 값은 저장
			scrapedResult.getDividends().stream()
				.map(x -> new DividendEntity(company.getId(), x))
				.forEach(x -> {
					boolean exists = dividendRepository.existsByCompanyIdAndDate(x.getCompanyId(),
						x.getDate());
					if (!exists) {
						dividendRepository.save(x); //중복이 아니면 DividendEntity 저장
						log.info("insert new dividend -> {}", x.toString());
					}
				});
			// 스크래핑 대상 사이트 서버에 연속적인 요청을 날리지 않도록 텀을 준다.
			try {
				Thread.sleep(3000); // 3 seconds
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		log.info("scraping scheduler is ended");
	}
}
