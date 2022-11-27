package com.zerobase.cms.order_api.application;

import com.zerobase.cms.order_api.domain.model.Product;
import com.zerobase.cms.order_api.domain.model.ProductItem;
import com.zerobase.cms.order_api.domain.product.AddProductCartForm;
import com.zerobase.cms.order_api.domain.redis.Cart;
import com.zerobase.cms.order_api.exception.CustomException;
import com.zerobase.cms.order_api.service.CartService;
import com.zerobase.cms.order_api.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

import static com.zerobase.cms.order_api.exception.ErrorCode.ITEM_ENOUGH;
import static com.zerobase.cms.order_api.exception.ErrorCode.NOT_FOUND_PRODUCT;

@Service
@RequiredArgsConstructor
public class CartApplication {

    private final CartService cartService;
    private final ProductSearchService productSearchService;

    public Cart addCart(Long customerId, AddProductCartForm form) {
        Product product = productSearchService.getProduct(form.getId());
        Cart cart = cartService.getCart(customerId);
        if (cart != null && !addAble(cart, product, form)) {
            throw new CustomException(ITEM_ENOUGH);
        }

        return cartService.addCart(customerId, form);
    }

    private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
        Cart.Product cartProduct = cart.getProducts().stream().filter(p -> p.getId().equals(form.getId()))
                .findFirst().orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

        // Map으로 하는 이유는 속도 때문!
        // 현재 카트에 담아놓은 각 아이템의 수량
        Map<Long, Integer> cartItemCountMap = cartProduct.getItems().stream()
                .collect(Collectors.toMap(Cart.ProductItem::getId, Cart.ProductItem::getCount));

        // 각 아이템의 현재 남은 재고
        Map<Long, Integer> currentItemCountMap = product.getProductItemList().stream()
                .collect(Collectors.toMap(ProductItem::getId, ProductItem::getCount));

        // [현재 카트에 담아놓은 각 아이템의 수량 + 새로 카트에 담은 아이템 수량 >= 현재 아이템 재고] 체크
        return form.getItems().stream()
                .noneMatch(formItem -> {
                    Integer cartCount = cartItemCountMap.get(formItem.getId());
                    Integer currentCount = currentItemCountMap.get(formItem.getId());
                    return formItem.getCount() + cartCount >= currentCount;
                });
    }
}
