package com.zerobase.cms.user_api.service.customer;

import static com.zerobase.cms.user_api.exception.ErrorCode.LOGIN_CHECK_FAIL;
import static com.zerobase.cms.user_api.exception.ErrorCode.NOT_AUTHORIZATION;

import com.zerobase.cms.user_api.domain.model.Customer;
import com.zerobase.cms.user_api.domain.repository.customer.CustomerRepository;
import com.zerobase.cms.user_api.exception.CustomException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;

	public Optional<Customer> findByIdAndEmail(Long id, String email) {
		return customerRepository.findById(id)
			.stream()
			.filter(x -> x.getEmail().equals(email)).findFirst();
	}

	public Customer findValidCustomerByEmailAndPassword(String email, String password) {
		Customer customer = customerRepository.findByEmail(email).stream()
			.filter(c -> c.getPassword().equals(password)).findFirst()
			.orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));

		if (!customer.isVerify()) {
			throw new CustomException(NOT_AUTHORIZATION);
		}
		return customer;
	}
}
