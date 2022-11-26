package com.zerobase.cms.order_api.domain.repository;

import com.zerobase.cms.order_api.domain.model.ProductItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

	Optional<ProductItem> findByName(String name);
	Optional<ProductItem> findBySellerIdAndId(Long sellerId, Long id);

}
