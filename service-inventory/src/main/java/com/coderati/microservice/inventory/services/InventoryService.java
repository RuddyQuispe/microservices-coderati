package com.coderati.microservice.inventory.services;

import com.coderati.microservice.inventory.dto.InventoryRequest;
import com.coderati.microservice.inventory.dto.InventoryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface InventoryService {

    Mono<InventoryResponse> createInventory(Mono<InventoryRequest> inventory);

    Mono<InventoryResponse> updateInventory(Mono<InventoryRequest> inventory);

    Flux<InventoryResponse> getAllInventories();

    Mono<InventoryResponse> getInventory(String code);


}
