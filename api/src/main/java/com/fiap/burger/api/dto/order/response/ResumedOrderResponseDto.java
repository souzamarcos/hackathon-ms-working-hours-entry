package com.fiap.burger.api.dto.order.response;

import com.fiap.burger.entity.order.Order;
import com.fiap.burger.entity.order.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;

public record ResumedOrderResponseDto(

    @NotNull
    Long id,

    @Null
    OrderCustomerResponseDto customer,

    @NotNull
    Double total,

    @NotNull
    OrderStatus status,

    LocalDateTime modifiedAt
) {

    public static ResumedOrderResponseDto toResponseDto(Order order) {
        return new ResumedOrderResponseDto(
            order.getId(),
            OrderCustomerResponseDto.toResponseDto(order.getCustomer()),
            order.getTotal(),
            order.getStatus(),
            order.getModifiedAt()
        );
    }
}
