package com.zerobase.cms.user_api.domain.customer;

import com.zerobase.cms.user_api.domain.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDto {

	private Long id;
	private String email;
	private Integer balance;

	public CustomerDto(Long id, String email) {
		this.id = id;
		this.email = email;
	}

	public static CustomerDto from(Customer customer) {
		return new CustomerDto(customer.getId(), customer.getEmail(),
			customer.getBalance() == null ? 0 : customer.getBalance());
	}

}
