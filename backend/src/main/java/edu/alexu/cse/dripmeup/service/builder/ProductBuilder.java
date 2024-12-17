package edu.alexu.cse.dripmeup.service.builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import edu.alexu.cse.dripmeup.dto.Product;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;

public class ProductBuilder implements ProductBuilderIF{
    private final ProductEntity productEntity;
    private final Product product;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ProductBuilder(Product product) {
        this.product = product;
        this.productEntity = new ProductEntity();
    }

    public ProductBuilder(ProductEntity productEntity, Product product) {
        this.product = product;
        this.productEntity = productEntity;
    }

    public void buildDescription() {
        this.productEntity.setDescription(this.product.getDescription());
    }

    public void buildTime(){
        LocalDateTime dateTime = LocalDateTime.parse(this.product.getDateOfCreation(), formatter);
        this.productEntity.setTime(dateTime);
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
