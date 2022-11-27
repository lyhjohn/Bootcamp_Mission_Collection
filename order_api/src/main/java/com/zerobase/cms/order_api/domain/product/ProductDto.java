package com.zerobase.cms.order_api.domain.product;

import com.zerobase.cms.order_api.domain.model.Product;
import com.zerobase.cms.order_api.domain.model.ProductItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private Long sellerId;
    private String name;
    private String description;
    @Default
    private List<ProductItemDto> productItemList = new ArrayList<>();

    public static ProductDto from(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .sellerId(product.getSellerId())
                .name(product.getName())
                .description(product.getDescription())
                .productItemList(
                        product.getProductItemList().stream()
                                .map(ProductItemDto::from).collect(Collectors.toList())
                )
                .build();
    }

    public static ProductDto withoutItemsFrom(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .sellerId(product.getSellerId())
                .name(product.getName())
                .description(product.getDescription())
                .build();
    }

    public static List<ProductDto> fromList(List<Product> products) {
        return products.stream().map(ProductDto::from).collect(Collectors.toList());
    }

    public static List<ProductDto> withoutProductItemsFromList(List<Product> products) {
        return products.stream().map(ProductDto::withoutItemsFrom).collect(Collectors.toList());
    }
}
