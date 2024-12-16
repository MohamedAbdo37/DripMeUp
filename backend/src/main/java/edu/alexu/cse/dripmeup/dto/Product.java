package edu.alexu.cse.dripmeup.dto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.alexu.cse.dripmeup.component.ShopManager;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import lombok.Getter;
import edu.alexu.cse.dripmeup.enumeration.ProductState;


public class Product {

    @Autowired
    private ShopManager shopManager;
    
    public Product(ProductEntity productEntity) {
        this.productID = productEntity.getProductID();
        this.price = productEntity.getPrice();
        this.state = productEntity.getState();
        this.description = productEntity.getDescription();
        this.dateOfCreation = productEntity.getTime().toString();
        this.variants = this.shopManager.getVariantsOfProduct(productEntity);

    }
    private final @Getter Long productID;
    private final @Getter double price;
    private final @Getter ProductState state;
    private final @Getter String description;
    private final @Getter String dateOfCreation;
    private final @Getter List<Variant> variants;
}
