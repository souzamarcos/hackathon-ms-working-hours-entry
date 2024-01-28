package com.fiap.burger.api.dto.order.request;

import com.fiap.burger.entity.order.Order;
import com.fiap.burger.entity.order.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.List;

public record OrderInsertRequestDto(
    @Null
    String customerToken,
    @NotNull
    List<OrderItemInsertRequestDto> items
) {
    public Order toEntity() {
        return new Order(
            customerToken,
            items().stream().map(OrderItemInsertRequestDto::toEntity).toList(),
            OrderStatus.AGUARDANDO_PAGAMENTO
        );
    }
}
