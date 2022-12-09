package com.zerobase.cms.order_api.domain.product;

import com.zerobase.cms.order_api.domain.model.ProductItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemDto {

	private Long id;
	private String name;
	private Integer count;
	private Integer price;

	public static ProductItemDto from(ProductItem item) {
		return ProductItemDto.builder()
			.id(item.getId())
			.name(item.getName())
			.price(item.getPrice())
			.count(item.getCount())
			.build();
	}
}
