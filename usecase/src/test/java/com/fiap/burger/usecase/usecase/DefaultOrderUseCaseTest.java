package com.fiap.burger.usecase.usecase;

import com.fiap.burger.entity.order.OrderStatus;
import com.fiap.burger.entity.product.Category;
import com.fiap.burger.usecase.adapter.gateway.OrderGateway;
import com.fiap.burger.usecase.adapter.gateway.ProductGateway;
import com.fiap.burger.usecase.misc.OrderBuilder;
import com.fiap.burger.usecase.misc.ProductBuilder;
import com.fiap.burger.usecase.misc.exception.EmptyAttributeException;
import com.fiap.burger.usecase.misc.exception.InvalidAttributeException;
import com.fiap.burger.usecase.misc.exception.NegativeOrZeroValueException;
import com.fiap.burger.usecase.misc.exception.NullAttributeException;
import com.fiap.burger.usecase.misc.exception.OrderNotFoundException;
import com.fiap.burger.usecase.misc.exception.TokenJwtException;
import com.fiap.burger.usecase.misc.token.TokenJwtSecret;
import com.fiap.burger.usecase.misc.secret.SecretUtils;
import com.fiap.burger.usecase.misc.token.TokenJwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultOrderUseCaseTest {
    private static final String TOKEN_SECRET = "TEST-SECRET";
    private static final String TOKEN_ISSUER = "TEST-ISSUER";

    @Mock
    OrderGateway orderGateway;

    @Mock
    ProductGateway productGateway;

    @Mock
    SecretUtils secretUtils;

    @InjectMocks
    DefaultOrderUseCase useCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        useCase = new DefaultOrderUseCase(orderGateway, productGateway, new TokenJwtUtils(secretUtils));
    }

    @Test
    void shouldFindById() {
        var id = 1L;
        var expected = new OrderBuilder().withId(id).build();

        when(orderGateway.findById(id)).thenReturn(expected);

        var actual = useCase.findById(id);

        assertEquals(expected, actual);

        verify(orderGateway, times(1)).findById(id);
    }

    @Test
    void shouldFindByAll() {
        var expected = Arrays.asList(new OrderBuilder().withId(1L).build(), new OrderBuilder().withId(2L).build());

        when(orderGateway.findAll()).thenReturn(expected);

        var actual = useCase.findAll();

        assertEquals(expected, actual);

        verify(orderGateway, times(1)).findAll();
    }

    @Test
    void shouldFindByAllWhenFindByStatusNull() {
        var expected = Arrays.asList(new OrderBuilder().withId(1L).build(), new OrderBuilder().withId(2L).build());

        when(orderGateway.findAll()).thenReturn(expected);

        var actual = useCase.findAllBy(null);

        assertEquals(expected, actual);

        verify(orderGateway, times(1)).findAll();
        verify(orderGateway, times(0)).findAllBy(any());
    }

    @Test
    void shouldFindByAllWhenFindByStatus() {
        var expected = Arrays.asList(new OrderBuilder().withId(1L).build(), new OrderBuilder().withId(2L).build());

        when(orderGateway.findAllBy(OrderStatus.AGUARDANDO_PAGAMENTO)).thenReturn(expected);

        var actual = useCase.findAllBy(OrderStatus.AGUARDANDO_PAGAMENTO);

        assertEquals(expected, actual);

        verify(orderGateway, times(0)).findAll();
        verify(orderGateway, times(1)).findAllBy(OrderStatus.AGUARDANDO_PAGAMENTO);
    }

    @Test
    void shouldFindAllInProgress() {
        var expected = Arrays.asList(new OrderBuilder().withId(1L).build(), new OrderBuilder().withId(2L).build());

        when(orderGateway.findAllInProgress()).thenReturn(expected);

        var actual = useCase.findAllInProgress();

        assertEquals(expected, actual);

        verify(orderGateway, times(1)).findAllInProgress();
    }

    @Test
    void shouldInsertOrder() {
        var orderToInsert = new OrderBuilder().toInsert();
        var order = new OrderBuilder().withTotal(40.0).build();
        var products = List.of(new ProductBuilder().withId(1L).withValue(30.0).build(), new ProductBuilder().withId(2L).withValue(10.0).withCategory(Category.ADICIONAL).build());
        var productIds = List.of(1L, 2L);

        when(productGateway.findByIds(productIds)).thenReturn(products);
        when(orderGateway.save(orderToInsert)).thenReturn(order);
        when(secretUtils.getTokenJwtSecret()).thenReturn(new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER));

        var actual = useCase.insert(orderToInsert);
        assertEquals(order, actual);

        verify(productGateway, times(1)).findByIds(productIds);
        verify(orderGateway, times(1)).save(orderToInsert);
    }

    @Test
    void shouldInsertOrderWithoutCustomer() {
        var orderToInsert = new OrderBuilder().withCustomer(null).toInsert();
        var order = new OrderBuilder().withCustomer(null).build();
        var products = List.of(new ProductBuilder().withId(1L).build(), new ProductBuilder().withId(2L).withCategory(Category.ADICIONAL).build());
        var productIds = List.of(1L, 2L);

        when(productGateway.findByIds(productIds)).thenReturn(products);
        when(orderGateway.save(orderToInsert)).thenReturn(order);

        var actual = useCase.insert(orderToInsert);

        assertEquals(order, actual);

        verify(productGateway, times(1)).findByIds(productIds);
        verify(orderGateway, times(1)).save(orderToInsert);
    }

    @Test
    void shouldThrownInvalidAttributeExceptionWhenOrderHasInvalidStatusToInsert() {
        var orderToInsert = new OrderBuilder().withStatus(OrderStatus.FINALIZADO).toInsert();
        var products = List.of(new ProductBuilder().withId(1L).build(), new ProductBuilder().withId(2L).withCategory(Category.ADICIONAL).build());
        var productIds = List.of(1L, 2L);

        when(productGateway.findByIds(productIds)).thenReturn(products);

        assertThrows(InvalidAttributeException.class, () -> useCase.insert(orderToInsert));

        verify(productGateway, times(0)).findByIds(any());
        verify(orderGateway, times(0)).save(any());
    }

    @Test
    void shouldThrownNullAttributeExceptionWhenOrderHasNullItemsToInsert() {
        var orderToInsert = new OrderBuilder().withItems(null).toInsert();

        assertThrows(NullAttributeException.class, () -> useCase.insert(orderToInsert));

        verify(productGateway, times(0)).findByIds(any());
        verify(orderGateway, times(0)).save(any());
    }

    @Test
    void shouldThrownEmptyAttributeExceptionWhenOrderHasNullItemsToInsert() {
        var orderToInsert = new OrderBuilder().withItems(Collections.emptyList()).toInsert();

        assertThrows(EmptyAttributeException.class, () -> useCase.insert(orderToInsert));

        verify(productGateway, times(0)).findByIds(any());
        verify(orderGateway, times(0)).save(any());
    }

    @Test
    void shouldThrownInvalidAttributeExceptionWhenOrderProductNotFound() {
        var orderToInsert = new OrderBuilder().toInsert();
        var products = List.of(new ProductBuilder().withId(2L).withCategory(Category.ADICIONAL).build());
        var productIds = List.of(1L, 2L);

        when(productGateway.findByIds(productIds)).thenReturn(products);
        when(secretUtils.getTokenJwtSecret()).thenReturn(new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER));

        assertThrows(InvalidAttributeException.class, () -> useCase.insert(orderToInsert));

        verify(productGateway, times(1)).findByIds(productIds);
        verify(orderGateway, times(0)).save(any());
    }

    @Test
    void shouldThrownInvalidAttributeExceptionWhenOrderProductHasInvalidCategory() {
        var orderToInsert = new OrderBuilder().toInsert();
        var products = List.of(new ProductBuilder().withId(1L).withCategory(Category.ADICIONAL).build(), new ProductBuilder().withId(2L).withCategory(Category.ADICIONAL).build());
        var productIds = List.of(1L, 2L);

        when(productGateway.findByIds(productIds)).thenReturn(products);
        when(secretUtils.getTokenJwtSecret()).thenReturn(new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER));

        assertThrows(InvalidAttributeException.class, () -> useCase.insert(orderToInsert));

        verify(productGateway, times(1)).findByIds(productIds);
        verify(orderGateway, times(0)).save(any());
    }

    @Test
    void shouldThrownInvalidAttributeExceptionWhenOrderAdditionalProductNotFound() {
        var orderToInsert = new OrderBuilder().toInsert();
        var products = List.of(new ProductBuilder().withId(1L).build());
        var productIds = List.of(1L, 2L);

        when(productGateway.findByIds(productIds)).thenReturn(products);
        when(secretUtils.getTokenJwtSecret()).thenReturn(new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER));

        assertThrows(InvalidAttributeException.class, () -> useCase.insert(orderToInsert));

        verify(productGateway, times(1)).findByIds(productIds);
        verify(orderGateway, times(0)).save(any());
    }

    @Test
    void shouldThrownInvalidAttributeExceptionWhenOrderAdditionalProductHasInvalidCategory() {
        var orderToInsert = new OrderBuilder().toInsert();
        var products = List.of(new ProductBuilder().withId(1L).build(), new ProductBuilder().withId(2L).withCategory(Category.LANCHE).build());
        var productIds = List.of(1L, 2L);

        when(productGateway.findByIds(productIds)).thenReturn(products);
        when(secretUtils.getTokenJwtSecret()).thenReturn(new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER));

        assertThrows(InvalidAttributeException.class, () -> useCase.insert(orderToInsert));

        verify(productGateway, times(1)).findByIds(productIds);
        verify(orderGateway, times(0)).save(any());
    }

    @Test
    void shouldThrowNegativeOrZeroValueExceptionWhenOrderTotalIsInvalidToInsert() {
        var orderToInsert = new OrderBuilder().toInsert();
        var products = List.of(new ProductBuilder().withId(1L).withValue(-10.0).build(), new ProductBuilder().withId(2L).withValue(-10.0).withCategory(Category.ADICIONAL).build());
        var productIds = List.of(1L, 2L);

        when(productGateway.findByIds(productIds)).thenReturn(products);
        when(secretUtils.getTokenJwtSecret()).thenReturn(new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER));

        assertThrows(NegativeOrZeroValueException.class, () -> useCase.insert(orderToInsert));

        verify(productGateway, times(1)).findByIds(productIds);
        verify(orderGateway, times(0)).save(any());
    }

    @Test
    void shouldCheckOutOrder() {
        var id = 1L;
        var order = new OrderBuilder().withId(id).withModifiedAt(LocalDateTime.of(2023, 9, 30, 0, 0, 0)).build();
        var modifiedAtBefore = order.getModifiedAt();

        when(orderGateway.findById(id)).thenReturn(order);

        var actual = useCase.checkout(id);

        assertEquals(OrderStatus.RECEBIDO, actual.getStatus());
        assertTrue(actual.getModifiedAt().isAfter(modifiedAtBefore));

        verify(orderGateway, times(1)).updateStatus(eq(id), eq(OrderStatus.RECEBIDO), any());
    }

    @Test
    void shouldThrownOrderNotFoundExceptionWhenOrderNotFoundToCheckout() {
        var id = 1L;

        when(orderGateway.findById(id)).thenReturn(null);

        assertThrows(OrderNotFoundException.class, () -> useCase.checkout(id));

        verify(orderGateway, times(0)).updateStatus(any(), any(), any());
    }

    @Test
    void shouldThrownAttributeExceptionWhenOrderStatusIsInvalidToCheckout() {
        var id = 1L;
        var order = new OrderBuilder().withId(id).withStatus(OrderStatus.FINALIZADO).build();

        when(orderGateway.findById(id)).thenReturn(order);

        assertThrows(InvalidAttributeException.class, () -> useCase.checkout(id));

        verify(orderGateway, times(0)).updateStatus(any(), any(), any());
    }

    @Test
    void shouldUpdateStatusOrder() {
        var id = 1L;
        var order = new OrderBuilder().withId(id).withStatus(OrderStatus.RECEBIDO).withModifiedAt(LocalDateTime.of(2023, 9, 30, 0, 0, 0)).build();
        var modifiedAtBefore = order.getModifiedAt();

        when(orderGateway.findById(id)).thenReturn(order);

        var actual = useCase.updateStatus(id, OrderStatus.EM_PREPARACAO);

        assertEquals(OrderStatus.EM_PREPARACAO, actual.getStatus());
        assertTrue(actual.getModifiedAt().isAfter(modifiedAtBefore));

        verify(orderGateway, times(1)).updateStatus(eq(id), eq(OrderStatus.EM_PREPARACAO), any());
    }

    @Test
    void shouldUpdateStatusOrderToCancelled() {
        var id = 1L;
        var order = new OrderBuilder().withId(id).withStatus(OrderStatus.AGUARDANDO_PAGAMENTO).withModifiedAt(LocalDateTime.of(2023, 9, 30, 0, 0, 0)).build();
        var modifiedAtBefore = order.getModifiedAt();

        when(orderGateway.findById(id)).thenReturn(order);

        var actual = useCase.updateStatus(id, OrderStatus.CANCELADO);

        assertEquals(OrderStatus.CANCELADO, actual.getStatus());
        assertTrue(actual.getModifiedAt().isAfter(modifiedAtBefore));

        verify(orderGateway, times(1)).updateStatus(eq(id), eq(OrderStatus.CANCELADO), any());
    }

    @Test
    void shouldThrownOrderNotFoundExceptionWhenOrderNotFoundToUpdateStatus() {
        var id = 1L;

        when(orderGateway.findById(id)).thenReturn(null);

        assertThrows(OrderNotFoundException.class, () -> useCase.updateStatus(id, OrderStatus.EM_PREPARACAO));

        verify(orderGateway, times(0)).updateStatus(any(), any(), any());
    }

    @Test
    void shouldThrownAttributeExceptionWhenOrderCancelledUpdateStatus() {
        var id = 1L;
        var order = new OrderBuilder().withId(id).withStatus(OrderStatus.CANCELADO).build();

        when(orderGateway.findById(id)).thenReturn(order);

        assertThrows(InvalidAttributeException.class, () -> useCase.updateStatus(id, OrderStatus.EM_PREPARACAO));

        verify(orderGateway, times(0)).updateStatus(any(), any(), any());
    }

    @Test
    void shouldThrownAttributeExceptionWhenOrderUpdateStatusToNotExpectedStep() {
        var id = 1L;
        var order = new OrderBuilder().withId(id).withStatus(OrderStatus.RECEBIDO).build();

        when(orderGateway.findById(id)).thenReturn(order);

        assertThrows(InvalidAttributeException.class, () -> useCase.updateStatus(id, OrderStatus.FINALIZADO));

        verify(orderGateway, times(0)).updateStatus(any(), any(), any());
    }

    @Test
    void shouldThrownAttributeExceptionWhenOrderUpdateStatusToAwaitingPayment() {
        var id = 1L;
        var order = new OrderBuilder().withId(id).withStatus(OrderStatus.AGUARDANDO_PAGAMENTO).build();

        when(orderGateway.findById(id)).thenReturn(order);

        assertThrows(InvalidAttributeException.class, () -> useCase.updateStatus(id, OrderStatus.RECEBIDO));

        verify(orderGateway, times(0)).updateStatus(any(), any(), any());
    }

    @Test
    void shouldThrownTokenJwtExceptionWhenTokenNotReturningCustomerId() {
        var tokenWithoutCustomerId = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBdWRpZW5jZSIsImlzcyI6IlRFU1QtSVNTVUVSIiwiY3BmIjoiMTY1NjU4MjQ3MzgiLCJpYXQiOjE2OTg2Mjg2NTksImp0aSI6Ijg5NjY2YTA2LTdkODgtNDBmNC1hYjBhLTI1ZTgyMGQ5N2FjNCJ9.LTAFfVwh0tNJtNLdBWHwvkUZIgbtO6x5UvPdwXEhz60";
        var orderToInsert = new OrderBuilder().withCustomerToken(tokenWithoutCustomerId).toInsert();
        var order = new OrderBuilder().withTotal(40.0).build();
        var products = List.of(new ProductBuilder().withId(1L).withValue(30.0).build(), new ProductBuilder().withId(2L).withValue(10.0).withCategory(Category.ADICIONAL).build());
        var productIds = List.of(1L, 2L);

        when(productGateway.findByIds(productIds)).thenReturn(products);
        when(orderGateway.save(orderToInsert)).thenReturn(order);
        when(secretUtils.getTokenJwtSecret()).thenReturn(new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER));

        assertThrows(TokenJwtException.class, () -> useCase.insert(orderToInsert));

        verify(productGateway, never()).findByIds(productIds);
        verify(orderGateway, never()).save(orderToInsert);
    }
}
