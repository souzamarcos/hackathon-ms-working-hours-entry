//package com.fiap.hackathon.application.utils;
//
//import com.fiap.hackathon.api.dto.order.request.WorkingHourRegistryRequestDto;
//import com.fiap.hackathon.api.dto.order.request.OrderItemInsertRequestDto;
//import com.fiap.hackathon.api.dto.order.request.OrderUpdateStatusRequestDto;
//import com.fiap.burger.entity.order.OrderPaymentStatus;
//import com.fiap.burger.entity.order.OrderStatus;
//import com.fiap.burger.listener.payment.PaymentMessageListenerDto;
//
//import java.util.Arrays;
//
//public class OrderHelper {
//
//    public static WorkingHourRegistryRequestDto createOrderRequest() {
//        var item = new OrderItemInsertRequestDto(2L, Arrays.asList(20L), "Comentário");
//
//        return new WorkingHourRegistryRequestDto(null, Arrays.asList(item));
//    }
//
//    public static OrderUpdateStatusRequestDto createUpdateStatusRequest(OrderStatus orderStatus) {
//        return new OrderUpdateStatusRequestDto(orderStatus);
//    }
//
//    public static PaymentMessageListenerDto createPaymentMessageListenerDto(Long orderId, OrderPaymentStatus paymentStatus) {
//        var genericPaymentId = 1L;
//        return new PaymentMessageListenerDto(genericPaymentId, orderId, paymentStatus);
//    }
//
//    public static WorkingHourRegistryRequestDto createOrderWithCustomerRequest() {
//        var item = new OrderItemInsertRequestDto(2L, Arrays.asList(20L), "Comentário");
//
//        return new WorkingHourRegistryRequestDto("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBdWRpZW5jZSIsImlzcyI6IlRFU1QtSVNTVUVSIiwiY3VzdG9tZXJJZCI6IjEiLCJjcGYiOiIxMjM0NTY3ODkwMSIsImlhdCI6MTcwNjQxMzQzMiwianRpIjoiZjA0MjNlNGMtN2MxMC00YjM1LThkOTUtZGJmYWZjM2NmZGE0In0.ofm4-HItY30TdyIzbqGgo-KXZf-OIlwhQckryBt52S8", Arrays.asList(item));
//    }
//
//}
