package com.zerobase.userapi.service.customer;

import static com.zerobase.userapi.exception.ErrorCode.LOGIN_CHECK_FAIL;

import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.domain.repository.customer.CustomerRepository;
import com.zerobase.userapi.exception.CustomException;
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
		return customerRepository.findByEmail(email).stream()
			.filter(c -> c.isVerify() && c.getPassword().equals(password)).findFirst()
			.orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));
	}
}
