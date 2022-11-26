package com.zerobase.cms.order_api.domain.model;

import com.zerobase.cms.order_api.domain.product.AddProductForm;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited // 변경 이력 추적(변경이력 저장한 테이블이 생성됨)
@AuditOverride(forClass = BaseEntity.class) // BaseEntity의 변경이력도 함께 추적
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long sellerId;
	private String name;
	private String description; // 상세설명

	// ProductItem 레파지토리에 따로 저장을 안해줘도, Product가 저장되면서 함께 저장된다!!!!!
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
	@Default
	private List<ProductItem> productItemList = new ArrayList<>();

	/**
	 * 파라미터가 한개면 from, 두개이상이면 from
	 */
	public static Product of(Long sellerId, AddProductForm form) {
		return Product.builder()
			.sellerId(sellerId)
			.name(form.getName())
			.description(form.getDescription())
			.productItemList(
				form.getAddProductItemFormList().stream()
					.map(x -> ProductItem.of(sellerId, x))
					.collect(Collectors.toList())
			)
			.build();
	}

	public Product addProductItem() {
		this.productItemList
			.forEach(x -> {
					x.setProduct(this);
				}
			);
		return this;
	}
}
