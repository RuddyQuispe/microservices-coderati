package com.coderati.microservice.order.services;

import com.coderati.microservice.order.dto.OrderRequest;
import com.coderati.microservice.order.dto.OrderResponse;
import com.coderati.microservice.order.model.entity.OrderModel;
import com.coderati.microservice.order.model.mapper.OrderMapper;
import com.coderati.microservice.order.proxy.api.InventoryApi;
import com.coderati.microservice.order.proxy.model.InventoryResponse;
import com.coderati.microservice.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.reactor.timelimiter.TimeLimiterOperator;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final InventoryApi inventoryApi;

    private final CircuitBreaker circuitBreaker;
    private final Retry retry;
    private final TimeLimiter timeLimiter;
    private final RateLimiter rateLimiter;

    @Override
    public Mono<OrderResponse> createOrder(OrderRequest orderDto) {
        OrderModel orderModel = OrderMapper.INSTANCE.requestToModel(orderDto);
        orderModel.setDateOrder(LocalDateTime.now().toString());
        orderModel.setStatusOrder(OrderResponse.StatusEnum.PENDING.getValue());

        return orderRepository.save(orderModel)
                .map(OrderMapper.INSTANCE::modelToResponse)
                .doOnSubscribe(p -> log.info("Saving order: {}", orderModel))
                .doOnSuccess(p -> log.info("Saved order: {}", orderModel))
                .doOnError(p -> log.error("Error saving order: {}", orderModel, p));
    }

    @Override
    public Mono<OrderResponse> getOrderById(int orderId) {
        return this.orderRepository.findById(orderId)
                .map(OrderMapper.INSTANCE::modelToResponse);
    }

    @Override
    public Flux<OrderResponse> getList() {
        return this.orderRepository.findAll()
                .map(OrderMapper.INSTANCE::modelToResponse);
    }

    @Override
    public Mono<OrderResponse> updateOrder(int orderId) {
        return this.orderRepository.findById(orderId)
                .map(p -> {
                    p.setStatusOrder(OrderResponse.StatusEnum.COMPLETED.getValue());
                    return p;
                })
                .flatMap(value -> this.invokeServices(value)
                        .map(x -> value)
                        .switchIfEmpty(Mono.empty())
                ).flatMap(this.orderRepository::save)
                .map(OrderMapper.INSTANCE::modelToResponse);
    }

    private Mono<InventoryResponse> invokeServices(OrderModel orderModel) {
        return this.inventoryApi.getInventory(orderModel.getCodeProduct())
                .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
//                .transformDeferred(RetryOperator.of(this.retry))
//                .transformDeferred(TimeLimiterOperator.of(this.timeLimiter))
//                .transformDeferred(RateLimiterOperator.of(this.rateLimiter))
                .onErrorResume(CallNotPermittedException.class, this::fallBackItem);
    }

    private Mono<InventoryResponse> fallBackItem(Throwable e) {
        log.error("Circuit breaker is open, falling back to default response: {}", e);
        return Mono.error(e);
        // TODO: llamar a otra api
    }
}
