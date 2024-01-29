package com.fiap.burger.usecase.usecase;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fiap.burger.entity.customer.Customer;
import com.fiap.burger.entity.order.Order;
import com.fiap.burger.entity.order.OrderStatus;
import com.fiap.burger.entity.product.Category;
import com.fiap.burger.entity.product.Product;
import com.fiap.burger.usecase.adapter.gateway.OrderGateway;
import com.fiap.burger.usecase.adapter.gateway.ProductGateway;
import com.fiap.burger.usecase.adapter.usecase.OrderUseCase;
import com.fiap.burger.usecase.misc.exception.*;
import com.fiap.burger.usecase.misc.token.TokenJwtUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DefaultOrderUseCase implements OrderUseCase {

    private static final String OLD_STATUS_FIELD = "oldStatus";
    private static final String NEW_STATUS_FIELD = "newStatus";
    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;
    private final TokenJwtUtils tokenJwtUtils;

    public DefaultOrderUseCase(OrderGateway orderGateway, ProductGateway productGateway,
                               TokenJwtUtils tokenJwtUtils) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
        this.tokenJwtUtils = tokenJwtUtils;
    }

    public Order findById(Long id) {
        return orderGateway.findById(id);
    }

    public List<Order> findAll() {
        return orderGateway.findAll();
    }

    public List<Order> findAllBy(OrderStatus status) {
        if (status == null) return orderGateway.findAll();
        return orderGateway.findAllBy(status);
    }

    public List<Order> findAllInProgress() {
        return orderGateway.findAllInProgress();
    }

    public Order insert(Order order) {
        validateOrderToInsert(order);
        Customer customer = getCustomer(order);
        List<Long> productIds = order.getProductIds();
        List<Product> products = productGateway.findByIds(productIds.stream().distinct().toList());
        validateProducts(order, products);
        var total = calculateTotal(productIds, products);
        validateTotal(total);
        order.setTotal(total);
        var persistedOrder = orderGateway.save(order);
        persistedOrder.setCustomer(customer);
        return persistedOrder;
    }

    public Order checkout(Long orderId) {
        var order = orderGateway.findById(orderId);

        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        validateCheckout(order);
        LocalDateTime now = LocalDateTime.now();
        orderGateway.updateStatus(order.getId(), OrderStatus.RECEBIDO, now);
        order.setStatus(OrderStatus.RECEBIDO);
        order.setModifiedAt(now);
        return order;
    }

    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        var order = orderGateway.findById(orderId);

        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        validateUpdateStatus(newStatus, order.getStatus());
        LocalDateTime now = LocalDateTime.now();
        orderGateway.updateStatus(order.getId(), newStatus, now);
        order.setStatus(newStatus);
        order.setModifiedAt(now);
        return order;
    }

    private void validateUpdateStatus(OrderStatus newStatus, OrderStatus oldStatus) {
        if (OrderStatus.CANCELADO.equals(oldStatus)) {
            throw new InvalidAttributeException("You can not change status of orders that are canceled.", OLD_STATUS_FIELD);
        }
        if (OrderStatus.AGUARDANDO_PAGAMENTO.equals(oldStatus) && !OrderStatus.CANCELADO.equals(newStatus)) {
            throw new InvalidAttributeException("You can not change status of orders that are awaiting payment.", OLD_STATUS_FIELD);
        }
        if (oldStatus.ordinal() + 1 != newStatus.ordinal() && !(OrderStatus.AGUARDANDO_PAGAMENTO.equals(oldStatus) && OrderStatus.CANCELADO.equals(newStatus))) {
            throw new InvalidAttributeException(String.format("Next status from '%s' should not be '%s'", oldStatus.name(), newStatus.name()), NEW_STATUS_FIELD);
        }
    }

    private void validateCheckout(Order order) {
        if (!order.canBePaid()) {
            throw new InvalidAttributeException("You can only check out orders that are awaiting payment.", OLD_STATUS_FIELD);
        }
    }

    private void validateProducts(Order order, List<Product> products) {
        order.getItems().forEach(item -> {
            Optional<Product> itemProduct = products.stream().filter(product -> product.getId().equals(item.getProductId())).findFirst();
            if (itemProduct.isEmpty()) {
                throw new InvalidAttributeException(String.format("Product '%s' not found.", item.getProductId()), "items.productId");
            }

            if (!validateCategory(itemProduct.get().getCategory(), false, !Optional.ofNullable(item.getAdditionalIds()).orElse(Collections.emptyList()).isEmpty())) {
                throw new InvalidAttributeException(String.format("Product '%s' has invalid category for an item.'", item.getProductId()), "items.productId");
            }

            Optional.ofNullable(item.getAdditionalIds()).orElse(Collections.emptyList()).forEach(additional -> {
                Optional<Product> additionalProduct = products.stream().filter(product -> product.getId().equals(additional)).findFirst();
                if (additionalProduct.isEmpty()) {
                    throw new InvalidAttributeException(String.format("Product '%s' not found.", additional), "items.additionalIds");
                }

                if (!validateCategory(additionalProduct.get().getCategory(), true, false)) {
                    throw new InvalidAttributeException(String.format("Product '%s' has invalid category for an additional.'", additional), "items.additionalIds");
                }
            });

        });
    }

    private boolean validateCategory(Category category, boolean isAdditional, boolean hasAdditional) {
        List<Category> itemCategories = List.of(Category.LANCHE, Category.ACOMPANHAMENTO, Category.BEBIDA, Category.SOBREMESA);
        List<Category> additionalCategories = List.of(Category.ADICIONAL);
        List<Category> itemWithAdditionalsCategories = List.of(Category.LANCHE);
        if (isAdditional) {
            return additionalCategories.contains(category);
        } else {
            if (hasAdditional) {
                return itemWithAdditionalsCategories.contains(category);
            }
            return itemCategories.contains(category);
        }


    }

    private Double calculateTotal(List<Long> productIds, List<Product> products) {
        return productIds.stream().mapToDouble(id -> products.stream().filter(product -> product.getId().equals(id)).findFirst().map(Product::getPrice).orElse(0.0)).sum();
    }

    private Customer getCustomer(Order order) {
        if (Optional.ofNullable(order.getCustomerToken()).isPresent()) {
            String customerId = extractIdFromToken(order.getCustomerToken());
            return new Customer(customerId);
        }
        return null;
    }

    protected String extractIdFromToken(String token) {
        DecodedJWT decodedJwt = tokenJwtUtils.readToken(token);
        return Optional.ofNullable(decodedJwt.getClaim("customerId").asString()).orElseThrow(() -> new TokenJwtException("Malformed token."));
    }

    private void validateOrderToInsert(Order order) {
        if (order.getStatus() != OrderStatus.AGUARDANDO_PAGAMENTO) {
            throw new InvalidAttributeException("Order should be created with status 'AGUARDANDO PAGAMENTO'", "id");
        }
        validateOrder(order);
    }

    private void validateOrder(Order order) {
        if (order.getItems() == null) {
            throw new NullAttributeException("items");
        }
        if (order.getItems().isEmpty()) {
            throw new EmptyAttributeException("items");
        }
    }

    private void validateTotal(Double total) {
        if (total <= 0) throw new NegativeOrZeroValueException("total");
    }

}
