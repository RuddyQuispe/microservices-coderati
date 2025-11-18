package com.coderati.microservice.inventory.repository;

import com.coderati.microservice.inventory.model.entity.ProductModel;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface InventoryRepository extends R2dbcRepository<ProductModel, Integer> {

    Mono<Boolean> existsByCode(String code);

    Mono<ProductModel> findByCode(String idProduct);


}
