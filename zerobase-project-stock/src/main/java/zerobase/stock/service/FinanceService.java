package zerobase.stock.service;

import static zerobase.stock.model.constants.CacheKey.KEY_FINANCE;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import zerobase.stock.exception.impl.NoCompanyException;
import zerobase.stock.model.Company;
import zerobase.stock.model.Dividend;
import zerobase.stock.model.ScrapedResult;
import zerobase.stock.persist.CompanyRepository;
import zerobase.stock.persist.DividendRepository;
import zerobase.stock.persist.entity.CompanyEntity;
import zerobase.stock.persist.entity.DividendEntity;

@Service
@AllArgsConstructor
@EnableCaching
@Slf4j
public class FinanceService {

	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;

	//요청이 자주 들어오는가? -> O
	// 자주 변경되는 데이터인가? -> X
	// 데이터를 캐시에 저장해놓고 캐시에서 꺼내오기에 적합하다고 판단 (데이터가 변경된다면 캐시를 비워야함)
	// config 파일을 이용해서 redis에 캐시가 저장되도록 설정했음
	// 캐시에 데이터를 저장해놓기 때문에 DB를 없애도 여전히 캐시에는 값이 있음(레디스에서 key값을 없애기 전까지)
	@Cacheable(key = "#companyName", value = KEY_FINANCE) // key: 캐시의 이름, finance: value 이름
	// allEntries: 캐시의 finance에 해당하는 데이터는 다 지움
	// 특정 키 해당하는 값을 지우려면 key: "키이름(캐시이름)"
	@CacheEvict(value = KEY_FINANCE, allEntries = true) // 캐시 지우는 어노테이션(메서드 동작 할 때마다 캐시를 비움)
//	@TimeToLive // 캐시 데이터의 유효 기간 설정 가능, config파일에서 .entryTtl(Duration.of()); 으로 삭제 주기를 설정해줘야함
	public ScrapedResult getDividendByCompanyName(String companyName) { // companyName이 value로 저장됨 ex) finance: 3M
		log.info("search company -> {}", companyName );
		// 1. 회사명의 회사 정보 조회
		CompanyEntity company = companyRepository.findByName(companyName)
			.orElseThrow(() -> new NoCompanyException());

		// 2. 회사의 배당금 조회
		List<DividendEntity> dividendEntities = dividendRepository.findAllByCompanyId(
			company.getId());

		// 3. 회사 정보와 배당금 정보를 ScrapedResult에 담아 return
		List<Dividend> dividendList = dividendEntities.stream().map(x ->
			new Dividend(x.getDate(), x.getDividend())
				).collect(Collectors.toList());

		return new ScrapedResult(new Company(company.getTicker(), company.getName()), dividendList);
	}
}
