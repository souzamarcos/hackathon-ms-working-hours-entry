package com.fiap.burger.controller.controller;

import com.fiap.burger.entity.customer.Customer;
import com.fiap.burger.entity.order.Order;
import com.fiap.burger.entity.order.OrderStatus;
import com.fiap.burger.gateway.order.gateway.DefaultCustomerGateway;
import com.fiap.burger.gateway.order.gateway.DefaultProductGateway;
import com.fiap.burger.messenger.notification.DefaultNotificationMessenger;
import com.fiap.burger.messenger.order.DefaultOrderMessenger;
import com.fiap.burger.usecase.misc.exception.OrderNotFoundException;
import com.fiap.burger.usecase.usecase.DefaultOrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DefaultOrderControllerTest {

    @Mock
    DefaultOrderUseCase useCase;

    @Mock
    DefaultOrderMessenger orderMessenger;

    @Mock
    DefaultNotificationMessenger notificationMessenger;

    @Mock
    DefaultProductGateway productGateway;

    @Mock
    DefaultCustomerGateway customerGateway;

    @InjectMocks
    DefaultOrderController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class findById {
        @Test
        void shouldFindById() {
            var id = 1L;
            var expected = new Order(null, Collections.emptyList(), OrderStatus.FINALIZADO);

            when(useCase.findById(id)).thenReturn(expected);
            when(productGateway.findByIds(anyList())).thenReturn(Collections.emptyList());

            Order actual = controller.findById(id);

            assertEquals(expected, actual);

            verify(useCase, times(1)).findById(id);
        }

        @Test
        void shouldThrownOrderNotFoundExceptionWhenOrderNotFoundById() {
            var id = 1L;

            when(useCase.findById(id)).thenReturn(null);

            assertThrows(OrderNotFoundException.class, () -> controller.findById(id));

            verify(useCase, times(1)).findById(id);
        }


        @Test
        void shouldFindByIdWithClient() {
            var id = 1L;
            var customer = new Customer("1", "43280829062", "email@email.com", "Cliente Exemplo 01");
            var order = new Order("1", Collections.emptyList(), OrderStatus.FINALIZADO);
            var expected = new Order("1", Collections.emptyList(), OrderStatus.FINALIZADO);
            expected.setCustomer(customer);

            when(useCase.findById(id)).thenReturn(order);
            when(productGateway.findByIds(anyList())).thenReturn(Collections.emptyList());
            when(customerGateway.findById("1")).thenReturn(customer);

            Order actual = controller.findById(id);

            assertEquals(expected, actual);

            verify(useCase, times(1)).findById(id);
        }
    }

    @Nested
    class findAllBy {
        @Test
        void shouldFindAllBy() {
            var expected = List.of(new Order(null, Collections.emptyList(), OrderStatus.FINALIZADO));

            when(useCase.findAllBy(OrderStatus.FINALIZADO)).thenReturn(expected);

            List<Order> actual = controller.findAllBy(OrderStatus.FINALIZADO);

            assertEquals(expected, actual);

            verify(useCase, times(1)).findAllBy(OrderStatus.FINALIZADO);
        }
    }

    @Nested
    class findAllInProgress {
        @Test
        void shouldFindAllInProgress() {
            var expected = List.of(new Order(null, Collections.emptyList(), OrderStatus.RECEBIDO));

            when(useCase.findAllInProgress()).thenReturn(expected);

            List<Order> actual = controller.findAllInProgress();

            assertEquals(expected, actual);

            verify(useCase, times(1)).findAllInProgress();
        }
    }

    @Nested
    class insertOrder {
        @Test
        void shouldInsertOrder() {
            var order = new Order(null, Collections.emptyList(), OrderStatus.RECEBIDO);

            when(useCase.insert(order)).thenReturn(order);

            Order actual = controller.insert(order);

            assertEquals(order, actual);

            verify(useCase, times(1)).insert(order);
            verify(orderMessenger, times(1)).sendMessage(order);
        }
    }

    @Nested
    class updateStatusOrder {
        @Test
        void shouldUpdateStatusOrderToEmPreparacao() {
            var expected = new Order(1L, new Customer("1"), Collections.emptyList(), 10.0, OrderStatus.EM_PREPARACAO, LocalDateTime.now(), null, null);

            when(useCase.updateStatus(1L, OrderStatus.EM_PREPARACAO)).thenReturn(expected);

            Order actual = controller.updateStatus(1L, OrderStatus.EM_PREPARACAO);

            assertEquals(expected, actual);

            verify(useCase, times(1)).updateStatus(1L, OrderStatus.EM_PREPARACAO);
            verify(notificationMessenger, never()).sendMessage(any());
        }

        @Test
        void shouldUpdateStatusOrderToPronto() {
            var expected = new Order(1L, new Customer("1"), Collections.emptyList(), 10.0, OrderStatus.PRONTO, LocalDateTime.now(), null, null);

            when(useCase.updateStatus(1L, OrderStatus.PRONTO)).thenReturn(expected);

            Order actual = controller.updateStatus(1L, OrderStatus.PRONTO);

            assertEquals(expected, actual);

            verify(useCase, times(1)).updateStatus(1L, OrderStatus.PRONTO);
            verify(notificationMessenger, times(1)).sendMessage(any());
        }

        @Test
        void shouldUpdateStatusOrderToFinalizado() {
            var expected = new Order(1L, null, Collections.emptyList(), 10.0, OrderStatus.FINALIZADO, LocalDateTime.now(), null, null);

            when(useCase.updateStatus(1L, OrderStatus.FINALIZADO)).thenReturn(expected);

            Order actual = controller.updateStatus(1L, OrderStatus.FINALIZADO);

            assertEquals(expected, actual);

            verify(useCase, times(1)).updateStatus(1L, OrderStatus.FINALIZADO);
            verify(notificationMessenger, never()).sendMessage(any());
        }
    }
    
    @Nested
    class checkout {
        @Test
        void shouldCheckoutOrderWithoutCustomer() {
            var order = new Order(1L, null, Collections.emptyList(), 10.0, OrderStatus.RECEBIDO, LocalDateTime.now(), null, null);

            when(useCase.checkout(1L)).thenReturn(order);

            Order actual = controller.checkout(1L);

            assertEquals(order, actual);

            verify(useCase, times(1)).checkout(1L);
            verify(notificationMessenger, never()).sendMessage(any());
        }

        @Test
        void shouldCheckoutOrderWithCustomer() {
            var order = new Order(1L, new Customer("1"), Collections.emptyList(), 10.0, OrderStatus.RECEBIDO, LocalDateTime.now(), null, null);

            when(useCase.checkout(1L)).thenReturn(order);

            Order actual = controller.checkout(1L);

            assertEquals(order, actual);

            verify(useCase, times(1)).checkout(1L);
            verify(notificationMessenger, times(1)).sendMessage(any());
        }
    }
}
