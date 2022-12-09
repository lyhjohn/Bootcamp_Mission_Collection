package com.zerobase.cms.order_api.controller;

import com.zerobase.cms.order_api.domain.product.ProductDto;
import com.zerobase.cms.order_api.service.ProductSearchService;
import com.zerobasedomain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search/product")
@RequiredArgsConstructor
public class SearchProductController {

    private final ProductSearchService searchService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> searchByName(@RequestParam(name = "name") String productName) {
        return ResponseEntity.ok(
                ProductDto.withoutProductItemsFromList(searchService.searchByName(productName))
        );
    }

    @GetMapping("/details")
    public ResponseEntity<ProductDto> searchDetails(@RequestParam(name = "id") Long productId) {
        return ResponseEntity.ok(
                ProductDto.from(searchService.getProduct(productId))
        );
    }


}
