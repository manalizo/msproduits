package com.mproduits.web.exceptions;

public class ProductNotFoundException extends RuntimeException {

    // Constructor to pass a custom error message
    public ProductNotFoundException(String message) {
        super(message); // Pass message to the parent (RuntimeException) constructor
    }

    // Optionally, add a constructor to pass both message and cause
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause); // Pass both message and cause to the parent (RuntimeException) constructor
    }
}
