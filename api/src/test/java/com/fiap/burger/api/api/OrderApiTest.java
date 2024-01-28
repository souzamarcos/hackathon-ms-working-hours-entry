package com.fiap.burger.api.api;

import com.fiap.burger.api.dto.order.request.OrderInsertRequestDto;
import com.fiap.burger.api.dto.order.request.OrderItemInsertRequestDto;
import com.fiap.burger.api.dto.order.request.OrderUpdateStatusRequestDto;
import com.fiap.burger.api.dto.order.response.*;
import com.fiap.burger.controller.adapter.api.OrderController;
import com.fiap.burger.entity.order.Order;
import com.fiap.burger.entity.order.OrderItem;
import com.fiap.burger.entity.order.OrderItemAdditional;
import com.fiap.burger.entity.order.OrderStatus;
import com.fiap.burger.entity.product.Category;
import com.fiap.burger.entity.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderApiTest {

    @InjectMocks
    OrderApi api;

    @Mock
    OrderController controller;

    @Test
    void shouldInsertOrder() {
        var request = new OrderInsertRequestDto(null, List.of(new OrderItemInsertRequestDto(1L, List.of(2L), "Comentário")));
        var order = getMockOrder();
        var expected = new ResumedOrderResponseDto(1L, null, 30.0, OrderStatus.AGUARDANDO_PAGAMENTO, null);

        when(controller.insert(request.toEntity())).thenReturn(order);

        ResumedOrderResponseDto actual = api.insert(request);

        assertEquals(expected, actual);

        verify(controller, times(1)).insert(request.toEntity());
    }

    @Test
    void shouldFindById() {
        var id = 1L;
        var order = getMockOrder();
        var expected = new OrderResponseDto(id, null, List.of(new OrderItemResponseDto(1L, "Nome", "Comentário", List.of(new OrderItemAdditionalResponseDto(2L, "Nome")))), 30.0, OrderStatus.AGUARDANDO_PAGAMENTO);

        when(controller.findById(id)).thenReturn(order);

        OrderResponseDto actual = api.findById(id);

        assertEquals(expected, actual);

        verify(controller, times(1)).findById(1L);
    }

    @Test
    void shouldUpdateStatus() {
        var order = getMockOrder();
        order.setStatus(OrderStatus.RECEBIDO);

        var expected = new ResumedOrderResponseDto(1L, null, 30.0, OrderStatus.RECEBIDO, null);

        when(controller.updateStatus(1L, OrderStatus.RECEBIDO)).thenReturn(order);

        ResumedOrderResponseDto actual = api.updateStatus(1L, new OrderUpdateStatusRequestDto(OrderStatus.RECEBIDO));

        assertEquals(expected, actual);

        verify(controller, times(1)).updateStatus(1L, OrderStatus.RECEBIDO);
    }

    @Test
    void shouldFindAllBy() {
        var id = 1L;
        var order = getMockOrder();
        var expected = List.of(new ResumedOrderResponseDto(id, null, 30.0, OrderStatus.AGUARDANDO_PAGAMENTO, null));

        when(controller.findAllBy(OrderStatus.AGUARDANDO_PAGAMENTO)).thenReturn(List.of(order));

        List<ResumedOrderResponseDto> actual = api.findAll(OrderStatus.AGUARDANDO_PAGAMENTO);

        assertEquals(expected, actual);

        verify(controller, times(1)).findAllBy(OrderStatus.AGUARDANDO_PAGAMENTO);
    }

    @Test
    void shouldFindAllInProgress() {
        var id = 1L;
        var order = getMockOrder();
        order.setStatus(OrderStatus.RECEBIDO);
        var expected = List.of(new ResumedOrderResponseDto(id, null, 30.0, OrderStatus.RECEBIDO, null));

        when(controller.findAllInProgress()).thenReturn(List.of(order));

        List<ResumedOrderResponseDto> actual = api.findAllInProgress();

        assertEquals(expected, actual);

        verify(controller, times(1)).findAllInProgress();
    }

    private Order getMockOrder() {
        return new Order(1L,
            null,
            List.of(new OrderItem(
                1L,
                1L,
                List.of(new OrderItemAdditional(
                    1L,
                    1L,
                    new Product(2L, Category.ADICIONAL, "Nome", "Descrição", 20.0)
                )),
                "Comentário",
                new Product(1L, Category.LANCHE, "Nome", "Descrição", 10.0))
            ), 30.0, OrderStatus.AGUARDANDO_PAGAMENTO, null, null, null);
    }
}
