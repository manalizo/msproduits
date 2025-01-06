package com.mproduits.service;

import com.mproduits.model.Product;
import com.mproduits.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductRepository productRepository;

    // Method to create a product with an image
    public Product createProduct(Product product, MultipartFile imageFile) throws IOException {
        // Save the image and get the file path
        String imagePath = imageService.saveImage(imageFile);

        // Store the image path in the product
        product.setImage(imagePath);

        // Save the product in the database
        return productRepository.save(product);
    }

    // Method to retrieve all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Method to retrieve a product by ID
    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }

    // Method to update a product's details, including its image
    public Product updateProduct(int id, Product updatedProduct, MultipartFile imageFile) throws IOException {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Update product details
        product.setTitre(updatedProduct.getTitre());
        product.setDescription(updatedProduct.getDescription());
        product.setPrix(updatedProduct.getPrix());

        // If there's a new image, save and update the image path
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete the old image
            imageService.deleteImage(product.getImage());

            // Save the new image and update the path
            String newImagePath = imageService.saveImage(imageFile);
            product.setImage(newImagePath);
        }

        return productRepository.save(product);
    }

    // Method to delete a product by ID
    public void deleteProduct(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Delete the associated image from the filesystem
        imageService.deleteImage(product.getImage());

        // Delete the product from the database
        productRepository.delete(product);
    }
}
