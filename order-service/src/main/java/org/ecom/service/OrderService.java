package org.ecom.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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


    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProduct")
    public Product getProductById(int productId){
        String url="http://localhost:8080/products/"+productId;
        return restTemplate.getForObject(url, Product.class);
    }

    public String placeOrder(int productId, int quantity){
        Product product=getProductById(productId);

        if(product==null) return "Product not found;";
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

    public Product fallbackProduct(int productId, Throwable ex) {
        Product dummy = new Product();
        dummy.setId(0);
        dummy.setName("Default Product (Service Down)");
        dummy.setPrice(0.0);
        return dummy;
    }
}
