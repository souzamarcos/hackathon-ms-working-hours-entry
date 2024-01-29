package com.fiap.burger.api.dto.order.response;

import com.fiap.burger.entity.order.Order;
import com.fiap.burger.entity.order.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.List;

public record OrderResponseDto(

    @NotNull
    Long id,

    @Null
    OrderCustomerResponseDto customer,

    @NotNull
    List<OrderItemResponseDto> items,

    @NotNull
    Double total,

    @NotNull
    OrderStatus status
) {

    public static OrderResponseDto toResponseDto(Order order) {
        return new OrderResponseDto(
            order.getId(),
            OrderCustomerResponseDto.toResponseDto(order.getCustomer()),
            order.getItems().stream().map(OrderItemResponseDto::toResponseDto).toList(),
            order.getTotal(),
            order.getStatus()
        );
    }
}