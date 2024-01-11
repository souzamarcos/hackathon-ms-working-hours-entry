package com.fiap.burger.controller.adapter.api;

import com.fiap.burger.entity.order.Order;
import com.fiap.burger.entity.order.OrderStatus;

import java.util.List;

public interface OrderController {
    Order insert(Order order);

    Order findById(Long orderId);

    Order updateStatus(Long orderId, OrderStatus newStatus);

    List<Order> findAllBy(OrderStatus status);

    List<Order> findAllInProgress();

    Order checkout(Long orderId);
}
