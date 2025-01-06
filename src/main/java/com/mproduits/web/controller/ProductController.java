package com.mproduits.web.controller;

import com.mproduits.model.Product;
import com.mproduits.service.ProductService;
import com.mproduits.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    // Endpoint to create a product with an image
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestParam("titre") String titre,
                                                 @RequestParam("description") String description,
                                                 @RequestParam("prix") Double prix,
                                                 @RequestParam("image") MultipartFile image) {
        try {
            Product product = new Product();
            product.setTitre(titre);
            product.setDescription(description);
            product.setPrix(prix);

            // Create and save the product with the uploaded image
            Product createdProduct = productService.createProduct(product, image);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Endpoint to get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint to update a product (including its image)
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id,
                                                 @RequestParam("titre") String titre,
                                                 @RequestParam("description") String description,
                                                 @RequestParam("prix") Double prix,
                                                 @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Product updatedProduct = new Product();
            updatedProduct.setTitre(titre);
            updatedProduct.setDescription(description);
            updatedProduct.setPrix(prix);

            Product product = productService.updateProduct(id, updatedProduct, image);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to serve the product image
    @GetMapping("/{id}/image")
    public ResponseEntity<FileSystemResource> getProductImage(@PathVariable int id) {
        // Fetch the product from the database
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Retrieve the image path from the product
        String imagePath = product.getImage();

        // Create a FileSystemResource for the image
        FileSystemResource resource = new FileSystemResource(new File(imagePath));

        // Return the image file with appropriate headers
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.IMAGE_JPEG)  // Adjust according to the image type
                .body(resource);
    }
}
