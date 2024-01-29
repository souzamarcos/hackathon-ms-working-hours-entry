package com.fiap.burger.gateway.order.gateway;

import com.fiap.burger.entity.customer.Customer;
import com.fiap.burger.usecase.adapter.gateway.CustomerGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class DefaultCustomerGateway implements CustomerGateway {
    @Value("${api.customer.host}")
    private String apiCustomerHost;

    @Autowired
    RestTemplate restTemplate;

    public Customer findById(String id) {
        ResponseEntity<Customer> responseEntity = restTemplate.exchange(
            buildFindByIdUrl(id),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {
            }
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            return null;
        }
    }

    private String buildFindByIdUrl(String id) {
        return apiCustomerHost + "/customers/" + id;
    }
}
