package com.zerobase.userapi.controller.customer;

import com.zerobase.userapi.domain.customer.ChangeBalanceForm;
import com.zerobase.userapi.domain.customer.CustomerDto;
import com.zerobase.userapi.domain.model.Customer;
import com.zerobase.userapi.exception.CustomException;
import com.zerobase.userapi.exception.ErrorCode;
import com.zerobase.userapi.service.customer.CustomerBalanceService;
import com.zerobase.userapi.service.customer.CustomerService;
import com.zerobasedomain.config.JwtAuthenticationProvider;
import com.zerobasedomain.domain.common.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

	private final JwtAuthenticationProvider provider;
	private final CustomerService customerService;
	private final CustomerBalanceService customerBalanceService;

	@GetMapping("/getInfo")
	public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X_AUTH_TOKEN") String token) {
		System.out.println("token = " + token);
		UserVo vo = provider.getUserVo(token);
		Customer customer = customerService.findByIdAndEmail(vo.getId(), vo.getEmail())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		return ResponseEntity.ok(CustomerDto.from(customer));
	}

	@PostMapping("/balance")
	public ResponseEntity<Integer> changeBalance(
		@RequestHeader(name = "X_AUTH_TOKEN") String token,
		@RequestBody ChangeBalanceForm form) {
		UserVo vo = provider.getUserVo(token);
		return ResponseEntity.ok(
			customerBalanceService.changeBalance(vo.getId(), form).getCurrentMoney());
	}
}
