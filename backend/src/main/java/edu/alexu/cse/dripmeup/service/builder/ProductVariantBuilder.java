package edu.alexu.cse.dripmeup.service.builder;

import edu.alexu.cse.dripmeup.entity.EntityIF;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import edu.alexu.cse.dripmeup.repository.ImageRepository;
import edu.alexu.cse.dripmeup.repository.ProductRepository;
import edu.alexu.cse.dripmeup.repository.VariantRepository;

public class ProductVariantBuilder implements ProductBuilderIF {
    
    private ProductRepository productRepository;


    private final VariantEntity variantEntity;

    public ProductVariantBuilder() {
        this.variantEntity = new VariantEntity();
    }

    public ProductVariantBuilder(ProductRepository productRepository, VariantRepository variantRepository, Long variantID) {
        this.variantEntity = variantRepository.findByVariantID(variantID);
        this.productRepository = productRepository;
    }

    public void buildPrice(double price) {
        this.variantEntity.setPrice(price);
    }

    public void buildColor(String color) {
        this.variantEntity.setColor(color);
    }

    public void buildWeight(String weight) {
        this.variantEntity.setWeight(weight);
    }   

    public void buildLength(String length) {
        this.variantEntity.setLength(length);
    }

    public void buildSize(String size) {
        this.variantEntity.setSize(size);
    }   

    public void buildStock(int stock) {
        this.variantEntity.setStock(stock);
    }

    public void buildSold(int sold) {
        this.variantEntity.setSold(sold);
    }

    public void buildState(ProductState state) {
        this.variantEntity.setState(state);
    }

    public void buildProduct(Long productID) {
        ProductEntity productEntity = this.productRepository.findByProductID(productID);
        this.variantEntity.setProduct(productEntity);
    }

    @Override
    public void build() {
        this.buildState(ProductState.ON_SALE);
        this.buildSold(0);
    }


    @Override
    public EntityIF getResult() {
        return this.variantEntity;
    }
}
