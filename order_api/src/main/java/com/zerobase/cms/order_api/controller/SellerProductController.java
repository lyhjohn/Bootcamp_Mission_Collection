package com.zerobase.cms.order_api.controller;

import com.zerobase.cms.order_api.domain.model.Product;
import com.zerobase.cms.order_api.domain.model.ProductItem;
import com.zerobase.cms.order_api.domain.product.AddProductForm;
import com.zerobase.cms.order_api.domain.product.AddProductItemForm;
import com.zerobase.cms.order_api.domain.product.ProductDto;
import com.zerobase.cms.order_api.domain.product.ProductItemDto;
import com.zerobase.cms.order_api.service.ProductItemService;
import com.zerobase.cms.order_api.service.ProductService;
import com.zerobasedomain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerProductController {

	private final ProductService productService;
	private final ProductItemService productItemService;
	private final JwtAuthenticationProvider provider;

	@PostMapping("/product")
	public ResponseEntity<ProductDto> addProduct(@RequestHeader(name = "X_AUTH_TOKEN") String token,
		@RequestBody AddProductForm form) {
		Product product = productService.addProduct(provider.getUserVo(token).getId(), form);
		return ResponseEntity.ok(ProductDto.from(product));
	}

	@PostMapping("/productItem")
	public ResponseEntity<ProductItemDto> addProductItem(@RequestHeader(name = "X_AUTH_TOKEN") String token,
		@RequestBody AddProductItemForm form) {
		ProductItem productItem = productItemService.addProductItem(provider.getUserVo(token).getId(), form);
		return ResponseEntity.ok(ProductItemDto.from(productItem));
	}
}
