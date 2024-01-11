package com.fiap.burger.gateway.order.gateway;

import com.fiap.burger.entity.product.Product;
import com.fiap.burger.usecase.adapter.gateway.ProductGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DefaultProductGateway implements ProductGateway {
    @Value("${api.product.host}")
    private String apiProductHost;

    @Autowired
    RestTemplate restTemplate;

    public List<Product> findByIds(List<Long> ids) {
        ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(
            buildFindByIdsUrl(ids),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            }
        );

        return responseEntity.getBody();
    }

    private String buildFindByIdsUrl(List<Long> ids) {
        return apiProductHost + "/products?id=" + ids.stream().map(Object::toString).collect(Collectors.joining(","));
    }
}
