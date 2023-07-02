package com.fiap.burger.domain.adapter.repository.order;

import com.fiap.burger.domain.entities.order.Order;
import com.fiap.burger.domain.entities.order.OrderStatus;
import java.util.List;

public interface OrderRepository {

    Order findById(Long id);
    List<Order> findAll();
    Order save(Order order);
    void updateStatus(Long id, OrderStatus newStatus);
}
