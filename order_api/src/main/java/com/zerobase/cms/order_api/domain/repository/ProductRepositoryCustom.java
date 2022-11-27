package com.zerobase.cms.order_api.domain.repository;

import com.zerobase.cms.order_api.domain.model.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> searchByName(String name);
}
