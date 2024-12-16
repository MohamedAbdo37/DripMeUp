package edu.alexu.cse.dripmeup.service.builder;

import edu.alexu.cse.dripmeup.entity.product.ProductEntity;

public abstract class ProductBuilder implements BuilderIF{
    private final ProductEntity product;

    public ProductBuilder() {
        this.product = new ProductEntity(); 
    }
    
    public ProductEntity getResult() {
        return this.product;
    }

}
