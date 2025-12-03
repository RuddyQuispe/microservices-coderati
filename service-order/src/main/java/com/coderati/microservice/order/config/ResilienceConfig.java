package com.coderati.microservice.order.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResilienceConfig {

    private static final String RESILIENCE_NAME = "microInventory";

    @Bean
    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return circuitBreakerRegistry.circuitBreaker(RESILIENCE_NAME);
    }

    @Bean
    public Retry remittanceServicesRetry(RetryRegistry registry) {
        return registry.retry(RESILIENCE_NAME);
    }

    @Bean
    public TimeLimiter remittanceServicesTimeLimiter(TimeLimiterRegistry registry) {
        return registry.timeLimiter(RESILIENCE_NAME);
    }

    @Bean
    public RateLimiter remittanceServiceRateLamitter(RateLimiterRegistry registry) {
        return registry.rateLimiter(RESILIENCE_NAME);
    }
}
