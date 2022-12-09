package com.zerobase.cms.order_api.service;

import com.zerobase.cms.order_api.domain.model.Product;
import com.zerobase.cms.order_api.domain.model.ProductItem;
import com.zerobase.cms.order_api.domain.product.AddProductItemForm;
import com.zerobase.cms.order_api.domain.product.UpdateProductItemForm;
import com.zerobase.cms.order_api.domain.repository.ProductItemRepository;
import com.zerobase.cms.order_api.domain.repository.ProductRepository;
import com.zerobase.cms.order_api.exception.CustomException;
import com.zerobase.cms.order_api.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.zerobase.cms.order_api.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductItemService {

    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;

    public ProductItem getProductItem(Long id) {
        return productItemRepository.findById(id)
                .orElseThrow(() -> new CustomException(NOT_FOUND_ITEM));
    }

    @Transactional
    public ProductItem saveProductItem(ProductItem productItem) {
        return productItemRepository.save(productItem);
    }

    @Transactional
    public ProductItem addProductItem(Long sellerId, AddProductItemForm form) {
        Product product = productRepository.findBySellerIdAndId(sellerId, form.getProductId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_ITEM));

        if (product.getProductItemList().stream()
                .anyMatch(item -> item.getName().equals(form.getName()))) {
            //하나라도 match되면 if 조건 충족
            throw new CustomException(SAME_ITEM_NAME);
        }

        if (productItemRepository.findByName(form.getName()).isPresent()) {
            throw new CustomException(SAME_ITEM_NAME);
        }

        ProductItem productItem = ProductItem.of(sellerId, form);
        product.getProductItemList().add(productItem);
        return productItemRepository.save(productItem);
    }

    @Transactional
    public ProductItem updateProductItem(Long sellerId, UpdateProductItemForm form) {
        ProductItem productItem = productItemRepository.findById(form.getId())
                .filter(x -> x.getSellerId().equals(sellerId))
                .orElseThrow(() -> new CustomException(NOT_FOUND_ITEM));

        productItem.setCount(form.getCount());
        productItem.setName(form.getName());
        productItem.setPrice(form.getPrice());
        return productItem;
    }

    @Transactional
    public void deleteProductItem(Long sellerId, Long productItemId) {
        ProductItem productItem = productItemRepository.findBySellerIdAndId(sellerId, productItemId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
        productItemRepository.delete(productItem);
    }
}
