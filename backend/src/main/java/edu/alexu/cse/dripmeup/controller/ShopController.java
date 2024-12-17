package edu.alexu.cse.dripmeup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.alexu.cse.dripmeup.component.ShopManager;
import edu.alexu.cse.dripmeup.dto.Product;
import edu.alexu.cse.dripmeup.dto.ProductSnapshot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
@RequestMapping("/api/1000/shop")
public class ShopController {

    @Autowired
    private ShopManager shopManager;

    @GetMapping("/products")
    public ResponseEntity<Page<ProductSnapshot>> getProducts(@RequestParam(name = "page") int page,
            @RequestParam("size") int size) {
        Page<ProductSnapshot> products = shopManager.getAllProducts(page, size);
        if (products == null || products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/product")
    public ResponseEntity<Product> getProduct(@RequestParam("productID") long productID) {
        Product product = shopManager.getProduct(productID);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping("/c/product")
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok("Product created successfully");
    }

}
