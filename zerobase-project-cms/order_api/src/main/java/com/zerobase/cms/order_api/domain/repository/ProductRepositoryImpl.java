package com.zerobase.cms.order_api.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.cms.order_api.domain.model.Product;
import com.zerobase.cms.order_api.domain.model.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zerobase.cms.order_api.domain.model.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> searchByName(String name) {
        String searchName = "%" + name + "%";

        return queryFactory.selectFrom(product)
                .where(product.name.like(searchName))
                .fetch();
    }
}
