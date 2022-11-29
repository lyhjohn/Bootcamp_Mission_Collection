package com.zerobase.cms.order_api.client.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CustomerDto {

	private Long id;
	private String email;
	private Integer balance;

}
