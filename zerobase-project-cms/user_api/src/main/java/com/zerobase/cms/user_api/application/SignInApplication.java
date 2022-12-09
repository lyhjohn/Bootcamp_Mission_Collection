package com.zerobase.cms.user_api.application;

import com.zerobase.cms.user_api.domain.SignInForm;
import com.zerobase.cms.user_api.domain.model.Customer;
import com.zerobase.cms.user_api.domain.model.Seller;
import com.zerobase.cms.user_api.service.customer.CustomerService;
import com.zerobase.cms.user_api.service.seller.SellerService;
import com.zerobasedomain.config.JwtAuthenticationProvider;
import com.zerobasedomain.domain.common.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {

	private final CustomerService customerService;
	private final SellerService sellerService;
	private final JwtAuthenticationProvider provider;

	public String customerLoginToken(SignInForm form) {
		// 1. 로그인 가능 여부
		Customer customer = customerService.findValidCustomerByEmailAndPassword(
			form.getEmail(), form.getPassword());
		// 2. 토큰 발행 - 로그인 유저 외에 다른 유저가 비정상적으로 접근하는것을 방지
		return provider.createToken(customer.getEmail(), customer.getId(), UserType.CUSTOMER);
	}

	public String sellerLoginToken(SignInForm form) {
		// 1. 로그인 가능 여부
		Seller seller = sellerService.findValidSeller(form.getEmail(), form.getPassword());
		// 2. 토큰 발행 - 로그인 유저 외에 다른 유저가 비정상적으로 접근하는것을 방지
		return provider.createToken(seller.getEmail(), seller.getId(), UserType.SELLER);
	}

}
