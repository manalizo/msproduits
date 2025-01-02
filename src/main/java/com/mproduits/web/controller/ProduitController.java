package com.mproduits.web.controller;

import com.mproduits.dao.ProduitDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produits")
public class ProduitController {

    @GetMapping
    public List<ProduitDTO> getAllProduits() {
        return List.of(
                new ProduitDTO(1L, "Produit 1", 100.0),
                new ProduitDTO(2L, "Produit 2", 200.0)
        );
    }
}
