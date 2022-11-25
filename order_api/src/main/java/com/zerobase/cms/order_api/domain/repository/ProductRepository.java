package com.zerobase.cms.order_api.domain.repository;

import com.zerobase.cms.order_api.domain.model.Product;
import com.zerobase.cms.order_api.domain.model.ProductItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

	/**
	 * fetch Lazy 설정을 무시하고 한번에 쿼리를 조회하고 싶을 때 사용
	 */
//	@EntityGraph(attributePaths = {"productItems"}, type = EntityGraphType.LOAD)
//	Optional<Product> findWithProductItemsById(Long id);

	Optional<Product> findBySellerId(Long sellerId);
}
