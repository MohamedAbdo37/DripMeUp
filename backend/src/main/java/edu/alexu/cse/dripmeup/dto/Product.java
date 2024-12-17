package edu.alexu.cse.dripmeup.dto;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.alexu.cse.dripmeup.component.ShopManager;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import edu.alexu.cse.dripmeup.enumeration.ProductState;

@NoArgsConstructor
@Data
public class Product {

    public Product(ProductEntity productEntity, ShopManager shopManager) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.productID = productEntity.getProductID();
        this.state = productEntity.getState();
        this.description = productEntity.getDescription();
        this.dateOfCreation = productEntity.getTime().format(formatter);
        this.variants = shopManager.getVariantsOfProduct(productEntity);
        this.rate = 5.0;
        this.numberOfFeedback = 1;
    }
    
    private @Setter @Getter Long productID;
    private @Setter @Getter ProductState state;
    private @Setter @Getter String description;
    private @Setter @Getter String dateOfCreation;
    private @Setter @Getter List<Variant> variants;
    private @Setter @Getter double rate;
    private @Setter @Getter int numberOfFeedback;
    
}
