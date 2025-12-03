package com.coderati.microservice.order.expose;

import com.coderati.microservice.order.api.OrdenesApiDelegate;
import com.coderati.microservice.order.dto.OrderRequest;
import com.coderati.microservice.order.dto.OrderResponse;
import com.coderati.microservice.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderApiImpl implements OrdenesApiDelegate {
    private final OrderService orderService;

    public Mono<OrderResponse> createOrder(Mono<OrderRequest> orderRequest, ServerWebExchange exchange) {
        return orderRequest.flatMap(orderService::createOrder);
    }

    public Mono<OrderResponse> getOrder(Integer orderId, ServerWebExchange exchange) {
        return this.orderService.getOrderById(orderId)
                .doOnSuccess(s -> {
                    if (Optional.ofNullable(s).isEmpty()) {
                        exchange.getResponse().setStatusCode(HttpStatus.NO_CONTENT);
                    }
                });
    }

    public Flux<OrderResponse> listOrders(ServerWebExchange exchange) {
        return this.orderService.getList();
    }

    public Mono<Void> updateOrder(Integer orderId, ServerWebExchange exchange) {
        return this.orderService.updateOrder(orderId)
                .then();
    }
}
