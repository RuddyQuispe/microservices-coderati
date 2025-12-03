package com.coderati.microservice.inventory.controller;

import com.coderati.microservice.inventory.api.InventoryApiController;
import com.coderati.microservice.inventory.dto.InventoryResponse;
import com.coderati.microservice.inventory.expose.InventoryApiImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.*;

@WebFluxTest(InventoryApiController.class)
@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockitoBean
    private InventoryApiImpl inventoryApi;

    @Test
    @DisplayName("get list inventiry")
    void getListInventoryWhenExistsThenReturnSuccess() {
        var inventory = new InventoryResponse()
                .idProduct("1");

        when(inventoryApi.listInventory(any()))
                .thenReturn(Flux.just(inventory));
        webTestClient.get()
                .uri("/services-inventory/inventories")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(InventoryResponse.class)
                .contains(inventory)
                .hasSize(1);

        verify(inventoryApi, times(1)).listInventory(any());
    }
}
