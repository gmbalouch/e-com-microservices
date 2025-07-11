package org.ecom.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.ecom.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductClient {

    @Autowired
    private RestTemplate restTemplate;

    @Retry(name = "productServiceRetry", fallbackMethod = "fallbackProduct")
    @CircuitBreaker(name = "productService")
    @RateLimiter(name = "productServiceRateLimiter")
    public Product getProductById(int productId) {
        System.out.println("Calling external product service...");
        String url = "http://localhost:8080/products/" + productId;
        return restTemplate.getForObject(url, Product.class);
    }

    public Product fallbackProduct(int productId, Throwable ex) {
        System.out.println("Fallback triggered due to: " + ex.getMessage());
        Product dummy = new Product();
        dummy.setName("Fallback Product");
        return dummy;
    }
}