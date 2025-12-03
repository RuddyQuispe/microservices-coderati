package com.coderati.microservice.inventory.services;

import com.coderati.microservice.inventory.dto.InventoryRequest;
import com.coderati.microservice.inventory.model.entity.ProductModel;
import com.coderati.microservice.inventory.model.mapper.InventoryMapper;
import com.coderati.microservice.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Test
    @DisplayName("Create inventory when parameter is correct then return success")
    void createInventoryWhenParameterIsCorrectThenReturnSuccess() {
        var requestInventory = new InventoryRequest()
                .idProduct("123")
                .nameProduct("Lentes")
                .price(BigDecimal.TEN)
                .stock(10);

        var productModel = ProductModel.builder()
                .id(1)
                .code("123")
                .nameProduct("Lentes")
                .stock(10)
                .price(BigDecimal.TEN)
                .build();

        var responseInventory = InventoryMapper.INSTANCE.modelToResponse(productModel);

        when(inventoryRepository.existsByCode(anyString()))
                .thenReturn(Mono.just(false));
        when(inventoryRepository.save(any(ProductModel.class)))
                .thenReturn(Mono.just(productModel));

//        inventoryService.createInventory(Mono.just(requestInventory))
//                .subscribe(
//                        response -> {
//                            assert response != null;
//                            assert response.getIdProduct().equals(responseInventory.getIdProduct());
//                            assert response.getNameProduct().equals(responseInventory.getNameProduct());
//                            assert response.getStock().equals(responseInventory.getStock());
//                        },
//                        error -> assert false
//                        () -> System.out.println("Test completed successfully.")
//                );

        var result = inventoryService.createInventory(Mono.just(requestInventory));

        StepVerifier.create(result)
                .expectNext(responseInventory)
                .verifyComplete();

        verify(inventoryRepository, times(1)).existsByCode("123");
        verify(inventoryRepository, times(1)).save(any(ProductModel.class));
    }

}