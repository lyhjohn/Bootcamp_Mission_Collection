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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.zerobase.cms.order_api.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CartApplication {

    private final CartService cartService;
    private final ProductSearchService productSearchService;

    public Cart addCart(Long customerId, AddProductCartForm form) {
        Product product = productSearchService.getProduct(form.getId());

        // redis 서버에서 customerId(PK)에 해당하는 Cart가 있는지 조회한다.
        Cart cart = cartService.getCart(customerId);

        // redis 서버에 있는 Cart에 아이템을  새로 만드
        if (cart != null && !addAble(cart, product, form)) {
            throw new CustomException(ITEM_ENOUGH);
        }

        return cartService.addCart(customerId, form);
    }

    private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
//        Cart.Product cartProduct =

        // Cart에 해당 Product가 이미 담겨있는지 확인
        Optional<Cart.Product> optionalProduct = cart.getProducts().stream().filter(p -> p.getId().equals(form.getId()))
                .findFirst();


        Map<Long, Integer> cartItemCountMap = new HashMap<>();

        // Map으로 하는 이유는 속도 때문!
        // Cart에 해당 Product가 이미 담겨있는 경우, Product의 수량을 Map으료 표시함
        if (optionalProduct.isPresent()) {
            Cart.Product cartProduct = optionalProduct.get();
            // 현재 카트에 담아놓은 각 아이템의 수량
            cartItemCountMap = cartProduct.getItems().stream()
                    .collect(Collectors.toMap(Cart.ProductItem::getId, Cart.ProductItem::getCount));
        }

        // 각 아이템의 현재 남은 재고
        Map<Long, Integer> currentItemCountMap = product.getProductItemList().stream()
                .collect(Collectors.toMap(ProductItem::getId, ProductItem::getCount));

        // 람다식에 넣기 위해 변수 재정의
        Map<Long, Integer> finalCartItemCountMap = cartItemCountMap;

        // [현재 카트에 담아놓은 각 아이템의 수량 + 새로 카트에 담은 아이템 수량 >= 현재 아이템 재고] 체크
        return form.getItems().stream()
                .noneMatch(formItem -> {
                    // 카트에 해당 아이템이 없는 경우 Cart에 담긴 해당 아이템의 수량을 0으로 표기.
                    Integer cartCount = finalCartItemCountMap.getOrDefault(formItem.getId(), 0);
                    Integer currentCount = currentItemCountMap.get(formItem.getId());
                    if (currentCount == null) {
                        throw new CustomException(NOT_FOUND_ITEM);
                    }
                    return formItem.getCount() + cartCount >= currentCount;
                });
    }
}
