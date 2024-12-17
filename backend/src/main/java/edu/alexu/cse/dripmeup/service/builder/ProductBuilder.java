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
        System.out.println(this.product.getDateOfCreation());
        LocalDateTime dateTime = LocalDateTime.parse(this.product.getDateOfCreation(), this.formatter);
        this.productEntity.setTime(dateTime);
    }

    public void buildState() {
        this.productEntity.setState(this.product.getState());
    }
    
    @Override
    public ProductEntity getResult() {
        System.out.println("##");
        System.out.println(this.productEntity.toString());
        return this.productEntity;
    }

    @Override
    public void build() {
        System.out.println("#"+5);
        this.buildTime();
        System.out.println("#"+5.1);
        this.buildDescription();
        System.out.println("#"+5.3);
        this.buildState();
        System.out.println("#"+5.5);
    }

}
