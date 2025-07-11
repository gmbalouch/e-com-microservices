package org.ecom.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.ecom.dto.Product;
import org.ecom.entity.Order;
import org.ecom.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repo;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductClient productClient;

    public String placeOrder(int productId, int quantity){

        Product product=productClient.getProductById(productId);

        if(product == null)
            return "Product not found with product id"+ productId;

        if ("Fallback Product".equals(product.getName()))
            return "Order failed,\nProduct service is down. Showing dummy product: " + product.getName();


        double total=product.getPrice()*quantity;

        Order order= new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotal(total);

        repo.save(order);

        return "Order placed { \nproduct: "+ product.getName()+", \ntotal amount: $"+total+", \n Quantity: "+ quantity+"\n}";

    }

    public List<Order> getAll(){
        return repo.findAll();
    }

    public Order getById(int id){

        return repo.findById(id).orElse(null);
    }

}
