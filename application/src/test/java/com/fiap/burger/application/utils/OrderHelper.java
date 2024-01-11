package com.fiap.burger.application.utils;

import com.fiap.burger.api.dto.order.request.OrderInsertRequestDto;
import com.fiap.burger.api.dto.order.request.OrderItemInsertRequestDto;

import java.util.Arrays;

public class OrderHelper {

    public static OrderInsertRequestDto createOrderRequest() {
        var item = new OrderItemInsertRequestDto(2L, Arrays.asList(20L), "Comentário");

        return new OrderInsertRequestDto(null, Arrays.asList(item));
    }

}
