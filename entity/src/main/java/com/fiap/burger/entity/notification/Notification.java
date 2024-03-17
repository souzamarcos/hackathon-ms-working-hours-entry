package com.fiap.burger.entity.notification;

import com.fiap.burger.entity.customer.Customer;
import com.fiap.burger.entity.order.Order;
import com.fiap.burger.entity.order.OrderStatus;

import java.util.Optional;

public class Notification {
    private Long orderId;
    private String customerId;
    private NotificationType notificationType;

    public Notification(Order order) {
        this.orderId = order.getId();
        this.customerId = Optional.ofNullable(order.getCustomer()).map(Customer::getId).orElse(null);
        this.notificationType = Optional.ofNullable(order.getStatus()).map(OrderStatus::asNotificationType).orElse(null);
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }
}
