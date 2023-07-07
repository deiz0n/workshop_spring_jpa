package com.deiz0n.webservices_spring_jpa.services;

import com.deiz0n.webservices_spring_jpa.models.Product;
import com.deiz0n.webservices_spring_jpa.repositories.ProductRepository;
import com.deiz0n.webservices_spring_jpa.services.exceptions.DatabaseException;
import com.deiz0n.webservices_spring_jpa.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Product insertProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProductById(UUID id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException error) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException error) {
            throw new DatabaseException(error.getMessage());
        }
    }

    public Product updateProduct(UUID id, Product newProductData) {
        try{
            var oldProductData = productRepository.getReferenceById(id);
            updateProductData(oldProductData, newProductData);
            return productRepository.save(newProductData);
        } catch (EntityNotFoundException error) {
            throw new ResourceNotFoundException(id);
        }
    }

    public void updateProductData(Product oldProductData, Product newProductData) {
        oldProductData.setName(newProductData.getName());
        oldProductData.setDescription(newProductData.getDescription());
        oldProductData.setPrice(newProductData.getPrice());
        oldProductData.setImgURL(newProductData.getImgURL());
    }
}
