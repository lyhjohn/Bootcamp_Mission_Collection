package com.zerobase.cms.order_api.service;

import com.zerobase.cms.order_api.domain.model.Product;
import com.zerobase.cms.order_api.domain.model.ProductItem;
import com.zerobase.cms.order_api.domain.product.AddProductForm;
import com.zerobase.cms.order_api.domain.product.UpdateProductForm;
import com.zerobase.cms.order_api.domain.product.UpdateProductItemForm;
import com.zerobase.cms.order_api.domain.repository.ProductRepository;
import com.zerobase.cms.order_api.exception.CustomException;
import com.zerobase.cms.order_api.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.zerobase.cms.order_api.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public Product findProduct(Long sellerId) {
        return productRepository.findBySellerId(sellerId).orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
    }

    @Transactional
    public Product addProduct(Long sellerId, AddProductForm form) {
        productRepository.findByName(form.getName()).ifPresent(x -> {
            throw new CustomException(SAME_ITEM_NAME);
        });
        Product product = productRepository.save(Product.of(sellerId, form));
        return product.addProductItem();
    }

    @Transactional
    public Product updateProduct(Long sellerId, UpdateProductForm form) {
        Product product = productRepository.findBySellerIdAndId(sellerId, form.getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

        product.setName(form.getName());
        product.setDescription(form.getDescription());

        for (UpdateProductItemForm itemForm : form.getUpdateProductItemForms()) {
            ProductItem productItem = product.getProductItemList().stream()
                    .filter(x -> x.getId().equals(itemForm.getId()))
                    .findFirst().orElseThrow(() -> new CustomException(NOT_FOUND_ITEM));
            productItem.setName(itemForm.getName());
            productItem.setCount(itemForm.getCount());
            productItem.setPrice(itemForm.getPrice());
        }
        return product;
    }
}
