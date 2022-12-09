package zerobase.stock.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zerobase.stock.model.Company;
import zerobase.stock.model.constants.CacheKey;
import zerobase.stock.persist.entity.CompanyEntity;
import zerobase.stock.service.CompanyService;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

	private final CompanyService companyService;
	private final CacheManager redisCacheManager;

	@GetMapping("/autocomplete")
	public ResponseEntity<?> autocomplete(@RequestParam String keyword) {

		// 키워드 받아 자동완성

		// Trie에 키워드를 저장해놓고 찾기 때문에 DB에서 찾지 않는다는 장점이 있음
//		var result = companyService.autocomplete(keyword); // Trie 라이브러리를 사용한 자동완성

		// Like 쿼리와 Pageable을 사용한 자동완성, DB에서 찾기 때문에 DB 부하가 커짐, 다만 구현이 간단하다는 장점
		var result = companyService.getCompanyNamesByKeyword(keyword);
		return ResponseEntity.ok(result);
	}

	@GetMapping
	@PreAuthorize("hasRole('READ')")
	public ResponseEntity<?> searchCompany(final Pageable pageable) {
		Page<CompanyEntity> companies = companyService.getAllCompany(pageable);
		return ResponseEntity.ok(companies);
	}

	@PostMapping
	@PreAuthorize("hasRole('WRITE')")
	public ResponseEntity<?> addCompany(@RequestBody Company req) {
		String ticker = req.getTicker().trim();
		log.info("addCompany start");

		if (ObjectUtils.isEmpty(ticker)) {
			throw new RuntimeException("ticker is empty");
		}

		Company company = companyService.save(ticker);

		// 회사 저장 시 키워드도 함께 저장
		companyService.addAutocompleteKeyword(company.getName());

		return ResponseEntity.ok(company);
	}

	@DeleteMapping
	@PreAuthorize("hasRole('WRITE')")
	public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
		String companyName = companyService.deleteCompany(ticker);
		clearFinanceCache(companyName); // 캐시에서도 지워줘야함.
		return ResponseEntity.ok(companyName);
	}

	public void clearFinanceCache(String companyName) {
		redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
	}
}
