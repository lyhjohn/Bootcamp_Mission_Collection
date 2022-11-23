package com.zerobase.userapi.domain.repository.customer;

import com.zerobase.userapi.domain.model.CustomerBalanceHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

public interface CustomerBalanceHistoryRepository extends
	JpaRepository<CustomerBalanceHistory, Long> {

	Optional<CustomerBalanceHistory> findFirstByCustomer_IdOrderByIdDesc(
		@RequestParam(name = "customer_id") Long customerId);
}
