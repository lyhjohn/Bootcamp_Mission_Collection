package com.zerobase.cms.order_api.service;

import com.zerobase.cms.order_api.client.RedisClient;
import com.zerobase.cms.order_api.domain.product.AddProductCartForm;
import com.zerobase.cms.order_api.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final RedisClient redisClient;

    public Cart getCart(Long customerId) {
        return redisClient.get(customerId, Cart.class);
    }

    @Transactional
    public Cart addCart(Long customerId, AddProductCartForm form) {

        Cart cart = redisClient.get(customerId, Cart.class);
        if (cart == null) {
            cart = new Cart();
            cart.setCustomerId(customerId);
        }
        // 장바구니에 같은 상품이 있는지 체크
        Optional<Cart.Product> optionalProduct = cart.getProducts().stream()
                .filter(p -> p.getId().equals(form.getId()))
                .findFirst();

        // 이전에 같은 Product 장바구니에 담은 적이 있다면 장바구니 내 해당 Product에 생긴 변화를 감지한다.
        if (optionalProduct.isPresent()) {
            Cart.Product redisProduct = optionalProduct.get();

            // 장바구니에 새로 담는 것을 요청받은 ProductItem 목록
            List<Cart.ProductItem> items = form.getItems().stream().map(Cart.ProductItem::from).collect(Collectors.toList());

            // 해당 클라이언트가 기존 장바구니에 담고있던 Product의 ProductItem 목록
            Map<Long, Cart.ProductItem> redisItemMap = redisProduct.getItems().stream()
                    .collect(Collectors.toMap(Cart.ProductItem::getId, item -> item));

            // 장바구니 내 담긴 Product와 새로 담은 Product의 id는 일치한데 이름이 바꼈다면
            if (!redisProduct.getName().equals(form.getName())) {
                cart.addMessage(redisProduct.getName() + "의 정보가 변경되었습니다. 확인 부탁드립니다.");
            }

            for (Cart.ProductItem item : items) {
                Cart.ProductItem redisItem = redisItemMap.get(item.getId());
                if (redisItem == null) {
                    // happy case - 기존 장바구니에 있는 것과 새로 담은 ProductItem이 겹치지 않는다면 추가로 담아준다.
                    redisProduct.getItems().add(item);
                } else { // 기존 장바구니에 있는 ProductItem과 새로 담은 것은 아이템이 같을 경우
                    // 가격이 다르다면
                    if (!redisItem.getPrice().equals(item.getPrice())) {
                        cart.addMessage(redisProduct.getName() + item.getName() + "의 가격이 변경되었습니다.");
                    }
                    redisItem.setCount(redisItem.getCount() + item.getCount());
                }
            }
        } else {
            // 이전에 같은 상품을 장바구니에 담은적이 없다면 장바구니에 해당 상품 추가
            Cart.Product product = Cart.Product.from(form);
            cart.getProducts().add(product);
        }
        redisClient.put(customerId, cart);
        return cart;
    }
}
