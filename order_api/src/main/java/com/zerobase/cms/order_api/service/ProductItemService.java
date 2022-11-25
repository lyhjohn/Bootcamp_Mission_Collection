package com.zerobase.cms.order_api.service;

import static com.zerobase.cms.order_api.exception.ErrorCode.NOT_FOUND_PRODUCT;
import static com.zerobase.cms.order_api.exception.ErrorCode.SAME_ITEM_NAME;

import com.zerobase.cms.order_api.domain.model.Product;
import com.zerobase.cms.order_api.domain.model.ProductItem;
import com.zerobase.cms.order_api.domain.product.AddProductItemForm;
import com.zerobase.cms.order_api.domain.repository.ProductItemRepository;
import com.zerobase.cms.order_api.domain.repository.ProductRepository;
import com.zerobase.cms.order_api.exception.CustomException;
import com.zerobase.cms.order_api.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductItemService {

	private final ProductItemRepository productItemRepository;
	private final ProductRepository productRepository;

	@Transactional
	public ProductItem addProductItem(Long sellerId, AddProductItemForm form) {
		Product product = productRepository.findBySellerId(sellerId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

		if (product.getProductItemList().stream()
			.anyMatch(item -> item.getName().equals(form.getName()))) {
			//하나라도 match되면 if 조건 충족
			throw new CustomException(SAME_ITEM_NAME);
		}

		if (productItemRepository.findByName(form.getName()).isPresent()) {
			throw new CustomException(SAME_ITEM_NAME);
		}

		ProductItem productItem = ProductItem.of(sellerId, form);
		productItem.setProduct(product);
		product.getProductItemList().add(productItem);
		return productItemRepository.save(productItem);
	}
}
