package edu.alexu.cse.dripmeup.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;

import edu.alexu.cse.dripmeup.component.ShopManager;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Data
public class Product {

    public Product(ProductEntity productEntity, ShopManager shopManager) {
        System.out.println("Product constructor DTO");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println(productEntity.getTime());
        this.productID = productEntity.getProductID();
        System.out.println(productEntity.getProductID());
        this.state = productEntity.getState();
        System.out.println(productEntity.getState());
        this.description = productEntity.getDescription();
        System.out.println(productEntity.getDescription());
        this.dateOfCreation = productEntity.getTime().format(formatter);
        System.out.println(productEntity.getTime().format(formatter));
        this.variants = shopManager.getVariantsOfProduct(productEntity);
        System.out.println("Variants added");
        this.rate = 5.0;
        this.numberOfFeedback = 1;
        this.categories = shopManager.getProductCategories(productEntity);
        System.out.println("Categories added"); 
    }
    
    private @Setter @Getter Long productID;
    private @Setter @Getter ProductState state;
    private @Setter @Getter String description;
    private @Setter @Getter String dateOfCreation;
    private @Setter @Getter List<Variant> variants;
    private @Setter @Getter double rate;
    private @Setter @Getter int numberOfFeedback;
    private @Setter @Getter List<Category> categories;
    
}
