package com.fiap.burger.messenger.order;

import com.fiap.burger.entity.order.Order;
import com.fiap.burger.messenger.adapter.OrderMessenger;
import com.fiap.burger.usecase.misc.profiles.Test;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Test
@Primary
@Service
public class InMemoryOrderMessenger implements OrderMessenger {
    public void sendMessage(Order order) {
        System.out.println("Order " + order.getId() + " was sent through messenger");
    }
}
