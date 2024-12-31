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
import edu.alexu.cse.dripmeup.dto.Category;
import edu.alexu.cse.dripmeup.dto.Product;
import edu.alexu.cse.dripmeup.dto.ProductSnapshot;
import edu.alexu.cse.dripmeup.dto.Variant;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import edu.alexu.cse.dripmeup.exception.ProductCreationException;
import edu.alexu.cse.dripmeup.service.ResponseBodyMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.alexu.cse.dripmeup.component.CategoryManager;


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

    @GetMapping("/category")
    public ResponseEntity<Page<ProductSnapshot>> getCategory(@RequestParam(name= "category") String category, 
                                        @RequestParam(name= "page") int page, @RequestParam("size") int size) {
        Page<ProductSnapshot> products = shopManager.getProductsByCategory(category, page, size);
        if ( products == null || products.isEmpty()) {
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
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product productSaved;
        try{
            productSaved = shopManager.createProduct(product);
        } catch(RuntimeException e){
    
            return ResponseEntity.badRequest().build();
        }
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
    
    @PostMapping("/random")
    public ResponseEntity<?> randomProducts() {
        try{
          // Define categories
            List<String> menCategories = List.of("Shirts", "Pants", "Suits");
            List<String> womenCategories = List.of("Tops", "Dresses", "Skirts");
            List<String> childrenCategories = List.of("T-shirts", "Shorts", "Outerwear", "Sleepwear");
    
            // Initialize products
            List<Product> products = new ArrayList<>();
    
            // Create products and variants for men categories
            int productCount = 1;
            for (String categoryName : menCategories) {
                for (int i = 0; i < 5; i++) { // 5 products per category
                    Product product = createProduct("Men's Product " + productCount++, categoryName, "High-quality men's product.");
                    Long productID = shopManager.createProduct(product).getProductID();
                    for(Variant v:createVariantsForProduct(product))
                        shopManager.crateVariant(v, productID);
                    products.add(product);
                }
            }
    
            // Create products and variants for women categories
            for (String categoryName : womenCategories) {
                for (int i = 0; i < 5; i++) { // 5 products per category
                    Product product = createProduct("Women's Product " + productCount++, categoryName, "Stylish women's product.");
                    Long productID = shopManager.createProduct(product).getProductID();
                    for(Variant v:createVariantsForProduct(product))
                        shopManager.crateVariant(v, productID);
                    products.add(product);
                }
            }
    
            // Create products and variants for children categories
            for (String categoryName : childrenCategories) {
                for (int i = 0; i < 5; i++) { // 5 products per category
                    Product product = createProduct("Children's Product " + productCount++, categoryName, "Durable and safe children's product.");
                    Long productID = shopManager.createProduct(product).getProductID();
                    for(Variant v:createVariantsForProduct(product))
                        shopManager.crateVariant(v, productID);
                    products.add(product);
                }
            }
    
            // Extra products to reach 55
            for (int i = 0; i < 10; i++) {
                String category = i % 2 == 0 ? "Accessories" : "Outerwear"; // Randomly assign extra categories
                Product product = createProduct("Extra Product " + productCount++, category, "Special edition product.");
                Long productID = shopManager.createProduct(product).getProductID();
                    for(Variant v:createVariantsForProduct(product))
                        shopManager.crateVariant(v, productID);
                    products.add(product);
            }
    
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok("done");
    }

    private Product createProduct(String name, String categoryName, String description) {
        CategoryManager categoryManager = new CategoryManager(); 
        Product product = new Product();
        product.setDescription(description);
        product.setState(ProductState.ON_SALE); // Assume ACTIVE is a valid state
        product.setDateOfCreation(java.time.LocalDate.now().toString());
        product.setRate(4.5); // Default rate
        product.setNumberOfFeedback(0); // No feedback initially
        product.setVariants(new ArrayList<>()); // Variants will be added later
        product.setCategories(List.of(categoryManager.getCategoryByName(categoryName))); // Assuming Category has this constructor
        return product;
    }

    private static List<Variant> createVariantsForProduct(Product product) {
        List<Variant> variants = new ArrayList<>();
    
        // Create 3 variants for each product
        for (int i = 1; i <= 3; i++) {
            Variant variant = new Variant();
            variant.setColor("Color " + i);
            variant.setWeight((i * 0.5) + " kg");
            variant.setLength((i * 10) + " cm");
            variant.setSize("Size " + i);
            variant.setStock(100 * i); // Stock increases for each variant
            variant.setSold(10 * i); // Simulate some sales
            variant.setState(ProductState.ON_SALE); // Assume ACTIVE is a valid state
            variant.setPrice(50.0 * i); // Increment price per variant
            variant.setDiscount(5.0 * i); // Increment discount per variant
            variant.setImages(List.of("image" + i + "_1.jpg", "image" + i + "_2.jpg")); // Placeholder images
            variants.add(variant);
        }
    
        return variants;
    }
}
