package com.zerobase.cms.order_api.service;

import com.zerobase.cms.order_api.domain.model.Product;
import com.zerobase.cms.order_api.domain.repository.ProductRepository;
import com.zerobase.cms.order_api.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zerobase.cms.order_api.exception.ErrorCode.NOT_FOUND_PRODUCT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductSearchService {
    private final ProductRepository productRepository;

    public List<Product> searchByName(String name) {
        return productRepository.searchByName(name);
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
    }

    public List<Product> getProductList(List<Long> productIds) {
        return productRepository.findAllById(productIds);
    }
}
