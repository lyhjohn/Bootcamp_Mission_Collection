package com.zerobase.cms.order_api.controller;

import com.zerobase.cms.order_api.domain.model.Product;
import com.zerobase.cms.order_api.domain.model.ProductItem;
import com.zerobase.cms.order_api.domain.product.*;
import com.zerobase.cms.order_api.service.ProductItemService;
import com.zerobase.cms.order_api.service.ProductService;
import com.zerobasedomain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerProductController {

    private final ProductService productService;
    private final ProductItemService productItemService;
    private final JwtAuthenticationProvider provider;

    @GetMapping("/product")
    public ResponseEntity<ProductDto> findProduct(@RequestHeader(name = "X_AUTH_TOKEN") String token, Long sellerId) {
        Product product = productService.findProduct(sellerId);
        return ResponseEntity.ok(ProductDto.from(product));
    }

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

    @PutMapping("/updateProduct")
    public ResponseEntity<ProductDto> updateProduct(@RequestHeader(name = "X_AUTH_TOKEN") String token,
                                                    @RequestBody UpdateProductForm form) {
        Product product = productService.updateProduct(provider.getUserVo(token).getId(), form);
        return ResponseEntity.ok(ProductDto.from(product));
    }

    @PutMapping("/updateProductItem")
    public ResponseEntity<ProductItemDto> updateProduct(@RequestHeader(name = "X_AUTH_TOKEN") String token,
                                                        @RequestBody UpdateProductItemForm form) {
        ProductItem productItem = productItemService.updateProductItem(provider.getUserVo(token).getId(), form);
        return ResponseEntity.ok(ProductItemDto.from(productItem));
    }

}
