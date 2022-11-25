package com.zerobase.cms.order_api.service;

import com.zerobase.cms.order_api.domain.model.Product;
import com.zerobase.cms.order_api.domain.product.AddProductForm;
import com.zerobase.cms.order_api.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional
	public Product addProduct(Long sellerId, AddProductForm form) {
		Product product = productRepository.save(Product.of(sellerId, form));
		return product.addProductItem();
	}
}
