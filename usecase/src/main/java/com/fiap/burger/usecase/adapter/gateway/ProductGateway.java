package com.fiap.burger.usecase.adapter.gateway;

import com.fiap.burger.entity.product.Product;

import java.util.List;

public interface ProductGateway {
    List<Product> findByIds(List<Long> ids);
}
