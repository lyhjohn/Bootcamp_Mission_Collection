package com.zerobase.cms.order_api.domain.model;

import com.zerobase.cms.order_api.domain.product.AddProductItemForm;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Audited
@AuditOverride(forClass = BaseEntity.class)
public class ProductItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long sellerId;
	private String name;
	private Integer price;
	private Integer count;

	// 연관된 엔티티에 엔티티 상태를 전파함(Transient, Persistent, Detached, Removed)
	// fetch는 연관된 엔티티를 한번에 가져올지 말지를 결정
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	/**
	 * 파라미터가 한개면 from, 두개이상이면 from
	 */
	public static ProductItem of(Long sellerId, AddProductItemForm form) {
		return ProductItem.builder()
			.sellerId(sellerId)
			.name(form.getName())
			.price(form.getPrice())
			.count(form.getCount())
			.build();
	}


}
