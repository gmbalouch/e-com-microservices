package org.ecom.controller;

import org.ecom.dto.Product;
import org.ecom.entity.Order;
import org.ecom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService service;

    @GetMapping("/health")
    public String health(){
        return "fine";
    }

    @GetMapping("/place/{productId}/{quantity}")
    public ResponseEntity<String> placeOrder(@PathVariable int productId, @PathVariable int quantity){
        try {
            String response = service.placeOrder(productId, quantity);
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            Product fallback=service.fallbackProduct(productId, e);
            return ResponseEntity.ok("Fallback used due to failure.\n" +
                    "Order placed {\nproduct: " + fallback.getName() +
                    ",\ntotal amount: $0.0,\nQuantity: " + quantity + "\n}");

        }

    }

    @GetMapping()
    public List<Order> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable int id){
        return service.getById(id);
    }

}