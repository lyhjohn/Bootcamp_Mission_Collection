package com.zerobase.cms.order_api.application;

import com.zerobase.cms.order_api.client.UserClient;
import com.zerobase.cms.order_api.client.user.ChangeBalanceForm;
import com.zerobase.cms.order_api.client.user.CustomerDto;
import com.zerobase.cms.order_api.domain.model.ProductItem;
import com.zerobase.cms.order_api.domain.redis.Cart;
import com.zerobase.cms.order_api.exception.CustomException;
import com.zerobase.cms.order_api.exception.ErrorCode;
import com.zerobase.cms.order_api.service.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.cms.order_api.exception.ErrorCode.NOT_ENOUGH_MONEY;
import static com.zerobase.cms.order_api.exception.ErrorCode.ORDER_FAIL_CHECK_CART;

@Service
@RequiredArgsConstructor
public class OrderApplication {

    private final CartApplication cartApplication;
    private final ProductItemService productItemService;
    private final UserClient userClient;

    @Transactional
    public void order(String token, Cart cart) {
        // case 1. 주문 시 기존 카트 버림
        // case 2. 주문하지 않은 아이템은 남겨둠
        // case 2는 개인적으로 구현해보면 좋을듯

        Cart orderCart = cartApplication.refreshCart(cart);

        // 문제가 있는 경우
        if (orderCart.getMessages().size() > 0) {
            throw new CustomException(ORDER_FAIL_CHECK_CART);
        }

        CustomerDto customerDto = userClient.getInfo(token).getBody();
        int totalPrice = cartApplication.getTotalPrice(orderCart);
        if (customerDto.getBalance() < totalPrice) {
            throw new CustomException(NOT_ENOUGH_MONEY);
        }

        // 거래 도중 출금하게 될 경우 잔액 부족에 따른 롤백 계획에 대해 생각할 필요가 있음
        userClient.changeBalance(token, ChangeBalanceForm.builder()
                .from("USER")
                .message("ORDER")
                .money(-totalPrice)
                .build());

        for (Cart.Product cartProduct : orderCart.getProducts()) {
            for (Cart.ProductItem cartItem : cartProduct.getItems()) {
                ProductItem productItem = productItemService.getProductItem(cartItem.getId());
                productItem.setCount(productItem.getCount() - cartItem.getCount());
            }
        }
    }



    // 결제를 위해 필요한 것
    // 1. 주문 가능한 상태인지
    // 2. 가격 변동이 있었는지
    // 3. 돈이 충분한지
    // 4. 재고관리
}
