package com.coderati.microservice.inventory.expose;

import com.coderati.microservice.inventory.api.InventoryApiDelegate;
import com.coderati.microservice.inventory.dto.InventoryRequest;
import com.coderati.microservice.inventory.dto.InventoryResponse;
import com.coderati.microservice.inventory.dto.OrderInvRequest;
import com.coderati.microservice.inventory.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryApiImpl implements InventoryApiDelegate {
    private final InventoryService inventoryService;

    @Override
    public Mono<InventoryResponse> getInventory(String productId, ServerWebExchange exchange) {
        return this.inventoryService.getInventory(productId)
                .doOnSubscribe(p -> log.info("Getting inventory {}", productId))
                .doOnSuccess(p -> log.info("Inventory found {}", productId))
                .doOnError(e -> log.error("Error getting inventory {}", productId, e));
    }

    @Override
    public Flux<InventoryResponse> listInventory(ServerWebExchange exchange) {
        return this.inventoryService.getAllInventories()
                .doOnSubscribe(p -> log.info("Listing all inventories"))
                .doOnNext(p -> log.info("Inventory found {}", p))
                .doOnComplete(() -> log.info("No inventories found"))
                .doOnCancel(() -> log.info("Listing of inventories cancelled"));
    }

    @Override
    public Mono<InventoryResponse> registerInventory(Mono<InventoryRequest> inventoryRequest,
                                                     ServerWebExchange exchange) {
        return this.inventoryService.createInventory(inventoryRequest)
                .doOnSubscribe(p -> log.info("Registering inventory {}", inventoryRequest))
                .doOnSuccess(p -> log.info("Inventory registered {}", inventoryRequest))
                .doOnError(e -> log.error("Error registering inventory {}", inventoryRequest, e));
    }

//    @Override
//    public Mono<Void> updateInventory(String productId,
//                                      Mono<OrderInvRequest> orderInvRequest,
//                                      ServerWebExchange exchange) {
//        return th
//    }
}
