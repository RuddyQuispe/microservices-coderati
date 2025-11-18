package com.coderati.microservice.inventory.services;

import com.coderati.microservice.inventory.dto.InventoryRequest;
import com.coderati.microservice.inventory.dto.InventoryResponse;
import com.coderati.microservice.inventory.model.entity.ProductModel;
import com.coderati.microservice.inventory.model.mapper.InventoryMapper;
import com.coderati.microservice.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public Mono<InventoryResponse> createInventory(Mono<InventoryRequest> inventory) {
        return inventory.map(InventoryMapper.INSTANCE::requestToModel)
                .flatMap(product ->
                        this.inventoryRepository.existsByCode(product.getCode())
                                .flatMap(exists -> {
                                    if (exists)
                                        return Mono.empty();
                                    return this.inventoryRepository.save(product);
                                })
                )
                .doOnSubscribe(p -> log.info("Creating inventory {}", inventory))
                .doOnSuccess(p -> log.info("Inventory created {}", inventory))
                .map(this.inventoryMapper::modelToResponse);
    }

    @Override
    public Mono<InventoryResponse> updateInventory(Mono<InventoryRequest> inventory) {
        return inventory.map(InventoryMapper.INSTANCE::requestToModel)
                .flatMap(product ->
                        this.inventoryRepository.existsByCode(product.getCode())
                                .flatMap(exists -> {
                                    if (exists)
                                        return this.inventoryRepository.save(product);
                                    return Mono.empty();
                                })
                )
                .doOnSubscribe(p -> log.info("Creating inventory {}", inventory))
                .doOnSuccess(p -> log.info("Inventory created {}", inventory))
                .map(this.inventoryMapper::modelToResponse);
    }

    @Override
    public Flux<InventoryResponse> getAllInventories() {
        return this.inventoryRepository.findAll().map(this.inventoryMapper::modelToResponse);
    }

    @Override
    public Mono<InventoryResponse> getInventory(String code) {
        return this.inventoryRepository.findByCode(code).map(this.inventoryMapper::modelToResponse);
    }
}
