package edu.alexu.cse.dripmeup.dto;


import edu.alexu.cse.dripmeup.component.ShopManager;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import lombok.Getter;

public class ProductSnapshot {
    

    public ProductSnapshot(ProductEntity productEntity, ShopManager shop) {
        this.productID = productEntity.getProductID();
        this.price = shop.getPriceOfProduct(productEntity);
        this.state = productEntity.getState();
        this.description = productEntity.getDescription();
        this.dateOfCreation = productEntity.getTime().toString();
        this.productImage = shop.getImageOfProduct(productEntity);
        this.discount = shop.getDicountOfProduct(productEntity);
    }

    private final @Getter Long productID;
    private final @Getter double price;
    private final @Getter double discount;
    private final @Getter ProductState state;
    private final @Getter String description;
    private final @Getter String dateOfCreation;
    private final @Getter String productImage;

}
