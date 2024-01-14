package com.fiap.burger.gateway.order.gateway;

import com.fiap.burger.entity.order.OrderStatus;
import com.fiap.burger.gateway.misc.OrderBuilder;
import com.fiap.burger.gateway.misc.OrderJPABuilder;
import com.fiap.burger.gateway.order.dao.OrderDAO;
import com.fiap.burger.gateway.order.model.OrderJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DefaultOrderGatewayTest {

    @Mock
    OrderDAO orderDAO;

    @InjectMocks
    DefaultOrderGateway gateway;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindById() {
        var id = 1L;
        var orderJPA = new OrderJPABuilder().withId(1L).build();
        var expected = orderJPA.toEntityWithItems();

        when(orderDAO.findById(id)).thenReturn(Optional.of(orderJPA));

        var actual = gateway.findById(id);

        assertEquals(expected, actual);

        verify(orderDAO, times(1)).findById(id);
    }

    @Test
    void shouldFindAll() {
        var ordersJPA = List.of(new OrderJPABuilder().withId(1L).build());
        var expected = ordersJPA.stream().map(OrderJPA::toEntity).collect(Collectors.toList());

        when(orderDAO.findAllByDeletedAtNull()).thenReturn(ordersJPA);

        var actual = gateway.findAll();

        assertEquals(expected, actual);

        verify(orderDAO, times(1)).findAllByDeletedAtNull();
    }

    @Test
    void shouldFindAllBy() {
        var ordersJPA = List.of(new OrderJPABuilder().withId(1L).withStatus(OrderStatus.RECEBIDO).build());
        var expected = ordersJPA.stream().map(OrderJPA::toEntity).collect(Collectors.toList());

        when(orderDAO.findAllByDeletedAtNullAndStatusEquals(OrderStatus.RECEBIDO)).thenReturn(ordersJPA);

        var actual = gateway.findAllBy(OrderStatus.RECEBIDO);

        assertEquals(expected, actual);

        verify(orderDAO, times(1)).findAllByDeletedAtNullAndStatusEquals(OrderStatus.RECEBIDO);
    }

    @Test
    void shouldFindAllInProgress() {
        var ordersJPA = List.of(new OrderJPABuilder().withId(1L).build());
        var expected = ordersJPA.stream().map(OrderJPA::toEntity).collect(Collectors.toList());

        when(orderDAO.findAllInProgress(Set.of(OrderStatus.RECEBIDO, OrderStatus.EM_PREPARACAO, OrderStatus.PRONTO))).thenReturn(ordersJPA);

        var actual = gateway.findAllInProgress();

        assertEquals(expected, actual);

        verify(orderDAO, times(1)).findAllInProgress(Set.of(OrderStatus.RECEBIDO, OrderStatus.EM_PREPARACAO, OrderStatus.PRONTO));
    }

    @Test
    void shouldSaveClient() {
        var orderJPA = new OrderJPABuilder().withId(1L).build();
        var order = new OrderBuilder().withId(null).build();
        var expected = new OrderBuilder().withId(1L).build();

        when(orderDAO.save(any())).thenReturn(orderJPA);

        var actual = gateway.save(order);

        assertEquals(expected.getId(), actual.getId());

        verify(orderDAO, times(1)).save(any());
    }

    @Test
    void shouldUpdateStatus() {
        gateway.updateStatus(1L, OrderStatus.RECEBIDO, LocalDateTime.now());
        verify(orderDAO, times(1)).updateStatus(eq(1L), eq(OrderStatus.RECEBIDO), any());
    }
}
