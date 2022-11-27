package com.zerobase.cms.order_api.controller;

import com.zerobase.cms.order_api.application.CartApplication;
import com.zerobase.cms.order_api.domain.product.AddProductCartForm;
import com.zerobase.cms.order_api.domain.redis.Cart;
import com.zerobase.cms.order_api.service.CartService;
import com.zerobasedomain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/cart")
@RequiredArgsConstructor
public class CustomCartController {

    private final CartApplication cartApplication;
    private final JwtAuthenticationProvider provider;

    @PostMapping
    public ResponseEntity<Cart> addCart(@RequestBody AddProductCartForm form,
                                        @RequestHeader(name = "X_AUTH_TOKEN") String token) {
        return ResponseEntity.ok(cartApplication.addCart(provider.getUserVo(token).getId(), form));
    }
}
