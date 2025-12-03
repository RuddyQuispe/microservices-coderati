package com.coderati.microservice.order.services;

import com.coderati.microservice.order.dto.OrderRequest;
import com.coderati.microservice.order.dto.OrderResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<OrderResponse> createOrder(OrderRequest orderDto);

    Mono<OrderResponse> getOrderById(int orderId);

    Flux<OrderResponse> getList();

    Mono<OrderResponse> updateOrder(int orderId);
}
