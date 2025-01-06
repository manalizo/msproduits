package com.mproduits.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    // Folder to store uploaded images
    private static final String IMAGE_DIR = "images/";

    // Method to save uploaded image and return its path
    public String saveImage(MultipartFile file) throws IOException {
        // Create a directory if it doesn't exist
        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }

        // Get the original file name
        String fileName = file.getOriginalFilename();

        // Create a path to save the file
        Path path = Paths.get(IMAGE_DIR + fileName);

        // Save the file to the server
        file.transferTo(path);

        // Return the relative path of the saved file
        return path.toString();
    }

    // Method to delete the image from the file system
    public void deleteImage(String imagePath) {
        File file = new File(imagePath);
        if (file.exists()) {
            file.delete();  // Delete the file if it exists
        }
    }
}
