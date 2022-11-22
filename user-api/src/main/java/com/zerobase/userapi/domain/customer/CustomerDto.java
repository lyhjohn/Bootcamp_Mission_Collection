package com.zerobase.userapi.domain.customer;

import com.zerobase.userapi.domain.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDto {

	private Long id;
	private String email;

	public static CustomerDto from(Customer customer) {
		return new CustomerDto(customer.getId(), customer.getEmail());
	}

}
