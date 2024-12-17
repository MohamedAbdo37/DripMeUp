package edu.alexu.cse.dripmeup.dto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.alexu.cse.dripmeup.component.ShopManager;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import lombok.Getter;
import edu.alexu.cse.dripmeup.enumeration.ProductState;

public class Product {

    public Product(ProductEntity productEntity, ShopManager shopManager) {
        this.productID = productEntity.getProductID();
        this.state = productEntity.getState();
        this.description = productEntity.getDescription();
        this.dateOfCreation = productEntity.getTime().toString();
        this.variants = shopManager.getVariantsOfProduct(productEntity);
        this.rate = 5.0;
        this.numberOfFeedback = 1;
    }

    private final @Getter Long productID;
    private final @Getter ProductState state;
    private final @Getter String description;
    private final @Getter String dateOfCreation;
    private final @Getter List<Variant> variants;
    private final @Getter double rate;
    private final @Getter int numberOfFeedback;

}