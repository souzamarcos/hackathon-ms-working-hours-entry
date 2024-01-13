package com.fiap.burger.gateway.order.gateway;

import com.fiap.burger.entity.product.Category;
import com.fiap.burger.entity.product.Product;
import com.fiap.burger.usecase.adapter.gateway.ProductGateway;
import com.fiap.burger.usecase.misc.profiles.Test;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Test
@Primary
@Service
public class InMemoryProductGateway implements ProductGateway {

    @Override
    public List<Product> findByIds(List<Long> ids) {
        var products = getProductMaps();
        return ids.stream().map(id -> products.getOrDefault(id, null)).filter(Objects::nonNull).toList();
    }

    private Map<Long, Product> getProductMaps() {
        Map<Long, Product> map = new HashMap<>();
        map.put(2L, new Product(2L, Category.LANCHE, "Lanche", "Lanche", 10.0));
        map.put(20L, new Product(20L, Category.ADICIONAL, "Adicional", "Adicional", 1.0));
        return map;
    }
}
