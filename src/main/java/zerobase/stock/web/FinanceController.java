package zerobase.stock.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zerobase.stock.model.ScrapedResult;
import zerobase.stock.service.FinanceService;

@RestController
@RequestMapping("/finance")
@AllArgsConstructor
@Slf4j
public class FinanceController {

	private final FinanceService financeService;

	@GetMapping("/dividend/{companyName}")
	public ResponseEntity<?> searchFinance(@PathVariable String companyName) {

		ScrapedResult result = financeService.getDividendByCompanyName(companyName);
		return ResponseEntity.ok(result);
	}
}
