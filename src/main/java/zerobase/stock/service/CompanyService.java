package zerobase.stock.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import zerobase.stock.exception.impl.NoCompanyException;
import zerobase.stock.model.Company;
import zerobase.stock.model.ScrapedResult;
import zerobase.stock.persist.CompanyRepository;
import zerobase.stock.persist.DividendRepository;
import zerobase.stock.persist.entity.CompanyEntity;
import zerobase.stock.persist.entity.DividendEntity;
import zerobase.stock.scraper.Scraper;

@Service
@AllArgsConstructor
@Slf4j
public class CompanyService {


	private final Scraper yahooFinanceScraper;
	private final CompanyRepository companyRepository;
	private final DividendRepository dividendRepository;
	private final Trie trie; // 자동완성을 위한 Trie는 앱에서 하나만 유지가 되어야 하므로 AppConfig 파일에서 빈으로 관리

	public Company save(String ticker) {

		boolean exists = this.companyRepository.existsByTicker(ticker);
		if (exists) {
			throw new RuntimeException("already exists ticker -> " + ticker);
		}
		return storeCompanyAndDividend(ticker);
	}

	public Page<CompanyEntity> getAllCompany(Pageable pageable) {
		return companyRepository.findAll(pageable);
	}

	private Company storeCompanyAndDividend(String ticker) {
		//ticker 기준으로 회사 name, ticker 스크래핑
		Company company = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);
		if (ObjectUtils.isEmpty(company)) { // 일치하는 회사가 없는 경우
			throw new RuntimeException("failed to scrap ticker -> " + ticker);
		}

		//회사가 존재할 경우 회사의 배당금 정보를 스크래핑
		ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(company);

		//스크래핑 한 회사 정보를 회사 테이블에 저장
		CompanyEntity companyEntity = companyRepository.save(new CompanyEntity(company));

		//스크래핑 한 배당금 정보를 회사 id와 함께 Entity에 담아 List로 만듬
		List<DividendEntity> dividendEntities = scrapedResult.getDividends().stream()
			.map(x -> new DividendEntity(companyEntity.getId(), x))
			.collect(Collectors.toList());

		//db 저장
		this.dividendRepository.saveAll(dividendEntities);
		return company;
	}

	public void addAutocompleteKeyword(String keyword) {
		trie.put(keyword, null); // 딱 자동완성 기능만 이용할 거기 때문에 value는 null처리
	}

	public List<String> autocomplete(String keyword) {
		return (List<String>) trie.prefixMap(keyword).keySet()
			.stream()
			.limit(10)
			.collect(Collectors.toList());
	}

	public void deleteAutocompleteKeyword(String keyword) {
		trie.remove(keyword);
	}

	public List<String> getCompanyNamesByKeyword(String keyword) {
		Pageable limit = PageRequest.of(0, 10);
		Page<CompanyEntity> companyEntities = companyRepository.findByNameStartingWithIgnoreCase(
			keyword, limit);
		return companyEntities.stream().map(CompanyEntity::getName).collect(Collectors.toList());
	}

	@Transactional
	public String deleteCompany(String ticker) {
		CompanyEntity companyEntity = companyRepository.findByTicker(ticker)
			.orElseThrow(() -> new NoCompanyException());
		dividendRepository.deleteAllByCompanyId(companyEntity.getId());
		companyRepository.delete(companyEntity);

		//자동완성에 저장한 것도 지워줘야함
		deleteAutocompleteKeyword(companyEntity.getName());
		return companyEntity.getName();
	}
}
