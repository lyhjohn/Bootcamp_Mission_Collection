package com.zerobase.userapi.service.customer;

import static com.zerobase.userapi.exception.ErrorCode.NOT_ENOUGH_MONEY;
import static com.zerobase.userapi.exception.ErrorCode.NOT_FOUND_USER;

import com.zerobase.userapi.domain.customer.ChangeBalanceForm;
import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.domain.model.CustomerBalanceHistory;
import com.zerobase.userapi.domain.repository.customer.CustomerBalanceHistoryRepository;
import com.zerobase.userapi.domain.repository.customer.CustomerRepository;
import com.zerobase.userapi.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerBalanceService {

	private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;
	private final CustomerRepository customerRepository;

	@Transactional(noRollbackFor = {CustomException.class}) // 해당 에러 발생 시 롤백 안함
	public CustomerBalanceHistory changeBalance(Long customerId, ChangeBalanceForm form)
		throws CustomException {
		System.out.println("실행1");
		CustomerBalanceHistory customerBalanceHistory
			= customerBalanceHistoryRepository.findFirstByCustomer_IdOrderByIdDesc(customerId)
			.orElseGet(() ->
				CustomerBalanceHistory.builder()
					.changeMoney(0)
					.currentMoney(0)
					.customer(customerRepository.findById(customerId)
						.orElseThrow(() -> new CustomException(NOT_FOUND_USER)))
					.build()
			);

		if (customerBalanceHistory.getCurrentMoney() + form.getMoney() < 0) {
			throw new CustomException(NOT_ENOUGH_MONEY);
		}
		customerBalanceHistory = CustomerBalanceHistory.builder()
			.changeMoney(form.getMoney())
			.currentMoney(customerBalanceHistory.getCurrentMoney() + form.getMoney())
			.description(form.getMessage())
			.fromMessage(form.getFrom())
			.customer(customerBalanceHistory.getCustomer())
			.build();

		Customer customer = customerBalanceHistory.getCustomer();
		customer.setBalance(customerBalanceHistory.getCurrentMoney());
		customerBalanceHistoryRepository.save(customerBalanceHistory);
		return customerBalanceHistory;
	}

}
