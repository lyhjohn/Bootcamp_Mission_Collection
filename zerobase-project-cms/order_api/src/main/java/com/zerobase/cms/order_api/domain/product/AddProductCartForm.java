package com.zerobase.cms.order_api.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductCartForm {
    // 장바구니에 Product 담는 Form
    private Long id;
    private Long sellerId;
    private String name;
    private String description;
    private List<ProductItem> items = new ArrayList<>();

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductItem {
        private Long id;
        private String name;
        private Integer count;
        private Integer price;
    }
}
