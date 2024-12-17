package edu.alexu.cse.dripmeup.dto;

import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import lombok.Getter;

public class Variant {
    
    public Variant(VariantEntity variantEntity) {
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
    }
    
    private final @Getter Long variantID;
    private final @Getter String color;
    private final @Getter String weight;
    private final @Getter String length;
    private final @Getter String size;
    private final @Getter int stock;
    private final @Getter int sold;
    private final @Getter ProductState state;
    private final @Getter double price;
    private final @Getter double discount;
}
