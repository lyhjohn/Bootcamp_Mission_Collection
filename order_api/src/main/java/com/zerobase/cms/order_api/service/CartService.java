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
    private int totalPrice = 0;

    public Cart getCart(Long customerId) {
        return redisClient.get(customerId, Cart.class);
    }

    @Transactional
    public Cart addCart(Long customerId, AddProductCartForm form) {

        // redis 서버로부터 PK(customerId)에 해당하는 Cart 데이터를 가져온다.
        Cart cart = redisClient.get(customerId, Cart.class);

        // redis 서버에 고객 소유의 Cart가 없으면 새로 객체를 생성해준다. (1인 최대 1 Cart)
        if (cart == null) {
            cart = new Cart();
            cart.setCustomerId(customerId);
        }
        // 장바구니에 같은 상품이 있는지 체크
        Optional<Cart.Product> optionalProduct = cart.getProducts().stream()
                .filter(p -> p.getId().equals(form.getId()))
                .findFirst();

        // redis에 고객 소유의 Cart가 이미 존재하면서, 새로 Cart에 추가한 아이템이 이미 Cart에 담겨 있는 경우
        // 이전에 같은 Product 장바구니에 담은 적이 있다면 장바구니 내 해당 Product에 생긴 변화를 감지한다.
        if (optionalProduct.isPresent()) {
            Cart.Product redisProduct = optionalProduct.get();

            // 장바구니에 담는 것을 요청받은 ProductItem 목록
            List<Cart.ProductItem> items = form.getItems().stream().map(Cart.ProductItem::from).collect(Collectors.toList());

            // redis에 저장된 해당 클라이언트의 기존 장바구니에 담겨있던 ProductItem 목록
            Map<Long, Cart.ProductItem> redisItemMap = redisProduct.getItems().stream()
                    .collect(Collectors.toMap(Cart.ProductItem::getId, item -> item));

            // 장바구니 내 담긴 Product와 새로 담은 Product의 id는 일치한데 이름이 바꼈다면
            if (!redisProduct.getName().equals(form.getName())) {
                cart.addMessage(redisProduct.getName() + "의 정보가 " + form.getName() + "으로 변경되었습니다. 확인 부탁드립니다.");
                redisProduct.setName(form.getName());
            }

            /**
             * cart -> redisProduct -> redisItemMap -> redisItem 순서로 결국 redis에서 뽑아낸것이기 떄문에 set을 통해 값 변경한게
             * db에 반영된다.
             */
            for (Cart.ProductItem item : items) {
                Cart.ProductItem redisItem = redisItemMap.get(item.getId());
                if (redisItem == null) {
                    // happy case - 기존 장바구니에 있는 것과 새로 담은 ProductItem이 겹치지 않는 경우
                    redisProduct.getItems().add(item);

                } else { // 기존 장바구니에 있는 ProductItem과 새로 담은 것은 아이템이 같을 경우
                    // 가격이 다르다면
                    if (!redisItem.getPrice().equals(item.getPrice())) {
                        cart.addMessage(item.getName() + "의 가격이 " + item.getPrice() + "으로 변경되었습니다.");
                        redisItem.setPrice(item.getPrice());
                    }
                    // 장바구니에 담긴 아이템 수량 증가
                    redisItem.setCount(redisItem.getCount() + item.getCount());
                }
                List<Cart.Product> products = cart.getProducts();
                for (Cart.Product product : products) {
                    product.getItems().forEach(x -> {
                        totalPrice += x.getPrice() * x.getCount();
                    });
                }
            }
        } else {
            // 고객 소유의 Cart가 redis서버에 없거나 이전에 같은 상품을 장바구니에 담은적이 없는 경우
            Cart.Product product = Cart.Product.from(form);
            cart.getProducts().add(product);
            form.getItems().forEach(x -> {
                totalPrice += x.getPrice() * x.getCount();
            });
        }

        cart.setTotalPrice(totalPrice);
        // redis 서버에 Cart 데이터 반영
        redisClient.put(customerId, cart);
        return cart;
    }
}
