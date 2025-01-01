package edu.alexu.cse.dripmeup.service.builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.alexu.cse.dripmeup.dto.Product;
import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;

public class ProductBuilder implements ProductBuilderIF{
    private final ProductEntity productEntity;
    private final Product product;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final List<CategoryEntity> categories;

    public ProductBuilder(Product product, List<CategoryEntity> categories) {
        this.product = product;
        this.productEntity = new ProductEntity();
        this.categories = categories;
    }

    public ProductBuilder(ProductEntity productEntity, Product product, List<CategoryEntity> categories) {
        this.product = product;
        this.productEntity = productEntity;
        this.categories = categories;
    }

    public void buildDescription() {
        this.productEntity.setDescription(this.product.getDescription());
    }

    public void buildTime(){
        LocalDateTime dateTime = LocalDateTime.parse(this.product.getDateOfCreation(), this.formatter);
        this.productEntity.setTime(dateTime);
    }

    public void buildState() {
        this.productEntity.setState(this.product.getState());
    }

    public void buildCategories() {
        if (this.categories == null) return;
        Set<CategoryEntity> categoriesSet = new HashSet<>();
        for(CategoryEntity c: this.categories){
            categoriesSet.add(c);
        }
        this.productEntity.setCategories(categoriesSet);
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
        this.buildCategories();
    }

}
