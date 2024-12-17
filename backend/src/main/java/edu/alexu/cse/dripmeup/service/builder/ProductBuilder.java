package edu.alexu.cse.dripmeup.service.builder;

import java.time.LocalDateTime;

import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;

public abstract class ProductBuilder implements BuilderIF{
    private final ProductEntity product;

    public ProductBuilder() {
        this.product = new ProductEntity(); 
    }

    public void buildDescription(String description) {
        this.product.setDescription(description);
    }

    public void buildTime(){
        this.product.setTime(localDateTime.now());
    }

    public void buildState(ProductState state) {
        this.product.setState(state);
    }
    
    public ProductEntity getResult() {
        return this.product;
    }

}
