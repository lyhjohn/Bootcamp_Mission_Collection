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

import java.util.*;
import java.util.stream.Collectors;

import static com.zerobase.cms.order_api.exception.ErrorCode.ITEM_ENOUGH;
import static com.zerobase.cms.order_api.exception.ErrorCode.NOT_FOUND_ITEM;

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

    public Cart getCart(Long customerId) {
        Cart cart = refreshCart(cartService.getCart(customerId));
        Cart returnCart = new Cart();
        returnCart.setCustomerId(customerId);
        returnCart.setProducts(cart.getProducts());
        returnCart.setMessages(cart.getMessages());
        returnCart.setTotalPrice(cartService.getTotalPrice(cart));
        cart.setMessages(new ArrayList<>());

        // redis에 저장
        cartService.putCart(customerId, cart);

        // returnCart 객체를 만들어 반환하고
        // redis에 저장되는 Cart의 message는 비워준다.
        return returnCart;
    }

    public Cart updateCart(Long customerId, Cart cart) {
        return cartService.putCart(customerId, cart);
    }

    /**
     * Cart 새로고침
     */
    private Cart refreshCart(Cart cart) {
        // 1. 상품이나 상품의 아이템의 정보, 가격, 수량 변경 체크 후 그에 맞는 알림 제공
        // 2. 변경된 상품의 수량, 가격을 db 반영

        // Cart 에 담은 Product id로 List 만든다.
        List<Long> cartProducts = cart.getProducts().stream().map(Cart.Product::getId).collect(Collectors.toList());

        // 장바구니에 담긴 모든 Product를 엔티티에서 꺼내온다.
        Map<Long, Product> dbProductMap = productSearchService.getProductList(cartProducts).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<Cart.Product> products = cart.getProducts();
        for (int i = 0; i < products.size(); i++) {


            Product dbProduct = dbProductMap.get(products.get(i).getId());
            if (dbProduct == null) {
                cart.addMessage(products.get(i).getName() + " 상품은 남아있는 수량이 없어 장바구니에서 제거됩니다.");
                products.remove(products.get(i));
                i--;
            } else {
                List<Cart.ProductItem> items = products.get(i).getItems();
                for (int j = 0; j < items.size(); j++) {

                    Cart.ProductItem productItem = items.get(j);

                    Map<Long, ProductItem> dbItemMap = dbProduct.getProductItemList().stream()
                            .collect(Collectors.toMap(ProductItem::getId, item -> item));

                    ProductItem dbProductItem = dbItemMap.get(productItem.getId());

                    if (dbProductItem == null) {
                        cart.addMessage(productItem.getName() + " 남아있는 옵션 수량이 없어 장바구니에서 제거됩니다.");
                        items.remove(productItem);
                        j--;
                    } else {
                        boolean isPriceChanged = !productItem.getPrice().equals(getDbItemPrice(dbItemMap, productItem.getId()));
                        boolean isCountNotEnough = productItem.getCount() > getDbItemCount(dbItemMap, productItem.getId());

                        if (isPriceChanged && isCountNotEnough) {
                            productItem.setPrice(dbProductItem.getPrice());
                            productItem.setCount(dbProductItem.getCount());
                            cart.addMessage(productItem.getName() + "가격과 수량이 변동되었습니다.");
                            cart.addMessage(productItem.getName() + "장바구니에 담은 수량이 부족하여 최대치로 조정됩니다.");

                        } else if (isPriceChanged) {
                            cart.addMessage(productItem.getName() + "가격이 변동되었습니다.");
                            productItem.setPrice(dbProductItem.getPrice());

                        } else if (isCountNotEnough) {
                            productItem.setCount(dbProductItem.getCount());
                            cart.addMessage(productItem.getName() + "수량이 변동되었습니다.");
                            cart.addMessage(productItem.getName() + "장바구니에 담은 수량이 부족하여 최대치로 조정됩니다.");
                        }
                    }
                }
            }

        }
        return cartService.putCart(cart.getCustomerId(), cart);
    }

    private int getDbItemPrice(Map<Long, ProductItem> redisItemMap, Long itemId) {
        return redisItemMap.get(itemId).getPrice();
    }

    private int getDbItemCount(Map<Long, ProductItem> redisItemMap, Long itemId) {
        return redisItemMap.get(itemId).getCount();
    }

    private boolean addAble(Cart cart, Product product, AddProductCartForm form) {
//        Cart.Product cartProduct =

        // Cart에 해당 Product가 이미 담겨있는지 확인
        Optional<Cart.Product> optionalProduct = cart.getProducts().stream().filter(p -> p.getId().equals(form.getId()))
                .findFirst();


        Map<Long, Integer> cartItemCountMap = new HashMap<>();

        // List 보다 Map으로 하는 이유는 속도 때문!
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
