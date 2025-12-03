package com.coderati.microservice.order.config;

import com.coderati.microservice.order.proxy.ApiClient;
import com.coderati.microservice.order.proxy.api.InventoryApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiClientConfig {
    @Value("${micro.inventory.url}")
    private String urlApiInventory;

    @Bean
    public InventoryApi inventoryApi(ApiClient apiClient) {
        return new InventoryApi(apiClient);
    }

    @Bean
    public ApiClient apiClient(WebClient webClient) {
        ApiClient apiClient = new ApiClient(webClient);
        apiClient.setBasePath(this.urlApiInventory);
        return apiClient;
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
}
