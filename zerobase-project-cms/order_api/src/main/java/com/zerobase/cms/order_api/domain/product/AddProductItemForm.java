package com.zerobase.cms.order_api.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductItemForm {
	private Long productId;
	private String name;
	private Integer price;
	private Integer count;

	public AddProductItemForm(String name, Integer price, Integer count) {
		this.name = name;
		this.price = price;
		this.count = count;
	}
}
