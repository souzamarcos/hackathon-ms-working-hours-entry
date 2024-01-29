package com.fiap.burger.gateway.order.gateway;

import com.fiap.burger.entity.customer.Customer;
import com.fiap.burger.usecase.adapter.gateway.CustomerGateway;
import com.fiap.burger.usecase.misc.profiles.Test;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Test
@Primary
@Service
public class InMemoryCustomerGateway implements CustomerGateway {
    @Override
    public Customer findById(String id) {
        var customers = getCustomerMaps();
        return customers.getOrDefault(id, null);
    }

    private Map<String, Customer> getCustomerMaps() {
        Map<String, Customer> map = new HashMap<>();
        map.put("1", new Customer("1", "43280829062", "email@email.com", "Cliente Exemplo 01"));
        return map;
    }
}
