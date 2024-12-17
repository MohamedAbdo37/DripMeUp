package edu.alexu.cse.dripmeup.service.builder;

import java.time.LocalDateTime;

import edu.alexu.cse.dripmeup.dto.Product;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;

public class ProductBuilder implements ProductBuilderIF{
    private final ProductEntity productEntity;
    private final Product product;
    
    public ProductBuilder(Product product) {
        this.product = product;
        this.productEntity = new ProductEntity();
    }

    public void buildDescription() {
        this.productEntity.setDescription(this.product.getDescription());
    }

    public void buildTime(){
        this.productEntity.setTime(LocalDateTime.now());
    }

    public void buildState() {
        this.productEntity.setState(this.product.getState());
    }
    
    @Override
    public ProductEntity getResult() {
        return this.productEntity;
    }

    @Override
    public void build() {
        this.buildTime();
        this.buildDescription();
        this.buildState();
    }

}
