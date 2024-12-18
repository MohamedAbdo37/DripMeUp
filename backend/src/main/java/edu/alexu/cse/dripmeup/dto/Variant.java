package edu.alexu.cse.dripmeup.dto;

import java.util.List;

import edu.alexu.cse.dripmeup.component.ShopManager;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Variant {
    
    public Variant(VariantEntity variantEntity, ShopManager shopManager) {
        this.variantID = variantEntity.getVariantID();
        this.color = variantEntity.getColor();
        this.price = variantEntity.getPrice();
        this.weight = variantEntity.getWeight();
        this.length = variantEntity.getLength();
        this.size = variantEntity.getSize();
        this.stock = variantEntity.getStock();
        this.sold = variantEntity.getSold();
        this.state = variantEntity.getState();
        this.discount = variantEntity.getDiscount();
        this.images = shopManager.getImagesOfVariant(variantEntity);
    }
    
    private @Setter @Getter Long variantID;
    private @Setter @Getter String color;
    private @Setter @Getter String weight;
    private @Setter @Getter String length;
    private @Setter @Getter String size;
    private @Setter @Getter int stock;
    private @Setter @Getter int sold;
    private @Setter @Getter ProductState state;
    private @Setter @Getter double price;
    private @Setter @Getter double discount;
    private @Setter @Getter List<String> images;
}
