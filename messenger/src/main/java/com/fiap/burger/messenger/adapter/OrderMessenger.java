package com.fiap.burger.messenger.adapter;

import com.fiap.burger.entity.order.Order;

public interface OrderMessenger {
    void sendMessage(Order order);
}
