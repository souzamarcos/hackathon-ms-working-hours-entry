package com.fiap.burger.usecase.adapter.gateway;

import com.fiap.burger.entity.customer.Customer;

public interface CustomerGateway {
    Customer findById(String id);
}
