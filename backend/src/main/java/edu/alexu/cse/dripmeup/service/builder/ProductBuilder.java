package edu.alexu.cse.dripmeup.service.builder;

import java.time.LocalDateTime;

import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;

public class ProductBuilder implements ProductBuilderIF{
    private final ProductEntity product;

    public ProductBuilder() {
        this.product = new ProductEntity(); 
    }

    public void buildDescription(String description) {
        this.product.setDescription(description);
    }

    public void buildTime(){
        this.product.setTime(LocalDateTime.now());
    }

    public void buildState(ProductState state) {
        this.product.setState(state);
    }
    
    @Override
    public ProductEntity getResult() {
        return this.product;
    }

    @Override
    public void build() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
