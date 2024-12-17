package edu.alexu.cse.dripmeup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.alexu.cse.dripmeup.component.ShopManager;
import edu.alexu.cse.dripmeup.dto.Product;
import edu.alexu.cse.dripmeup.dto.ProductSnapshot;
import edu.alexu.cse.dripmeup.dto.Variant;
import edu.alexu.cse.dripmeup.exception.ProductCreationException;
import java.io.IOException;



@RestController
@CrossOrigin
@RequestMapping("/api/1000/shop")
public class ShopController {
    
    @Autowired
    private ShopManager shopManager;
    
    @GetMapping("/products")
    public ResponseEntity<Page<ProductSnapshot>> getProducts(@RequestParam(name= "page") int page, @RequestParam("size") int size) {
        Page<ProductSnapshot> products = shopManager.getAllProducts(page, size);
        if ( products == null || products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/product")
    public ResponseEntity<Product> getProduct(@RequestParam("productID") long productID) {
        Product product = shopManager.getProduct(productID);
        if ( product == null ) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping("/c/product")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product productSaved = shopManager.createProduct(product);
        return ResponseEntity.ok(productSaved);
    }

    @PostMapping("/c/variant")
    public ResponseEntity<Variant> createVariant(@RequestParam("productID") long productID, @RequestBody Variant variant) {
        Variant variantSaved;
        try{
            variantSaved = shopManager.crateVariant(variant, productID);
        } catch (ProductCreationException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(variantSaved);
    }

    @PostMapping("/c/image")
    public ResponseEntity<Void> addImageToVariant(@RequestParam("variantID") long variantID ,@RequestBody byte[] image) {

        String imagePath ;
        try{
            imagePath = this.shopManager.getImagePath(image);
            shopManager.addImageToVariant(variantID, imagePath);
        } catch (ProductCreationException | IOException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
    
}

