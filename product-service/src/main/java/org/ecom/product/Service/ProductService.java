package org.ecom.product.Service;

import org.ecom.product.entity.Product;
import org.ecom.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;


    public List<Product> getALL(){
        return repo.findAll();
    }

    public Product getById(int id){
        return repo.findById(id).orElse(null);
    }

    public Product create(Product product){
        Product saved=repo.save(product);

        return saved;
    }

    public Product update(Product product){
        return repo.save(product);
    }

    public void delete(int id){
        repo.deleteById(id);
    }

 }