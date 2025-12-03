package com.coderati.microservice.order.repository;

import com.coderati.microservice.order.model.entity.OrderModel;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrderRepository extends R2dbcRepository<OrderModel, Integer> {
}
