package org.ecom.product.controller;

import org.ecom.product.Service.ProductService;
import org.ecom.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping("health")
    public String health(){
        return "Fine";
    }

    @GetMapping
    public List<Product> getAll(){
        return productService.getALL();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable int id){
        return productService.getById(id);
    }

    @PostMapping
    public Product create(@RequestBody Product product){
        Product saved=productService.create(product);
        System.out.println(saved.getName()+"\n"+saved.getPrice());
        return saved;
    }

    @PutMapping
    public Product update(@RequestBody Product product){
        return productService.update(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id){
        productService.delete(id);
    }
}
