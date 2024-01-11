package com.fiap.burger.messenger.order;

import com.fiap.burger.entity.order.Order;

public class OrderMessageDto {
    private Long id;

    public static OrderMessageDto toDto(Order order) {
        return new OrderMessageDto(order.getId());
    }

    public OrderMessageDto(Long id) {
        this.id = id;
    }
}
