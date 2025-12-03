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

    @Override
    public Mono<InventoryResponse> createInventory(Mono<InventoryRequest> inventory) {
        return inventory.map(InventoryMapper.INSTANCE::requestToModel)
                .flatMap(value -> inventoryRepository.existsByCode(value.getCode())
                        .flatMap(y -> {
                            if (y) {
                                return Mono.empty();
                            }
                            return inventoryRepository.save(value);
                        })
                ).map(InventoryMapper.INSTANCE::modelToResponse)
                .doOnSubscribe(p -> log.info("Creating inventory..."))
                .doOnSuccess(p -> log.info("Inventory created successfully."))
                .doOnError(ex -> log.error("Error creating inventory: {}", ex.getMessage()));
    }

    @Override
    public Mono<InventoryResponse> updateInventory(Mono<InventoryRequest> inventory) {
        return inventory.map(InventoryMapper.INSTANCE::requestToModel)
                .flatMap(value -> inventoryRepository.existsByCode(value.getCode())
                        .flatMap(y -> {
                            if (y) {
                                return Mono.empty();
                            }
                            return inventoryRepository.save(value);
                        })
                ).map(InventoryMapper.INSTANCE::modelToResponse)
                .doOnSubscribe(p -> log.info("Updating inventory..."))
                .doOnSuccess(p -> log.info("Inventory updated successfully."))
                .doOnError(ex -> log.error("Error updating inventory: {}", ex.getMessage()));
    }

    @Override
    public Mono<InventoryResponse> getInventory(String code) {
        return inventoryRepository.findByCode(code)
                .map(InventoryMapper.INSTANCE::modelToResponse);
    }

    @Override
    public Flux<InventoryResponse> getAllInventories() {
        return inventoryRepository.findAll()
                .map(InventoryMapper.INSTANCE::modelToResponse)
                .doOnComplete(() -> log.info("Fetched all inventory items."));
    }
}
