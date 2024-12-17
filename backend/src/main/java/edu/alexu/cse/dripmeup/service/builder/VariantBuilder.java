package edu.alexu.cse.dripmeup.service.builder;

import edu.alexu.cse.dripmeup.dto.Variant;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import edu.alexu.cse.dripmeup.repository.ProductRepository;

public class VariantBuilder implements ProductBuilderIF {
    
    private ProductRepository productRepository;


    private final VariantEntity variantEntity;
    private final Variant variant;
    private final ProductEntity product;

    public VariantBuilder(Variant variant, ProductEntity product) {
        this.variant = variant;
        this.variantEntity = new VariantEntity();
        this.product = product;
    }

    public void buildPrice() {
        this.variantEntity.setPrice(this.variant.getPrice());
    }

    public void buildColor() {
        this.variantEntity.setColor(this.variant.getColor());
    }

    public void buildWeight() {
        this.variantEntity.setWeight(this.variant.getWeight());
    }   

    public void buildLength() {
        this.variantEntity.setLength(this.variant.getLength());
    }

    public void buildSize() {
        this.variantEntity.setSize(this.variant.getSize());
    }   

    public void buildStock() {
        this.variantEntity.setStock(this.variant.getStock());
    }

    public void buildSold() {
        this.variantEntity.setSold(0);
    }

    public void buildState() {
        this.variantEntity.setState(ProductState.ON_SALE);
    }

    public void buildDiscount() {
        this.variantEntity.setDiscount(this.variant.getDiscount());
    }

    public void buildProduct() {
        this.variantEntity.setProduct(this.product);
        this.product.getVariants().add(this.variantEntity);
        this.productRepository.save(this.product);
    }

    @Override
    public void build(){
        this.buildPrice();
        this.buildColor();
        this.buildWeight();
        this.buildLength();
        this.buildSize();
        this.buildStock();
        this.buildSold();
        this.buildState();
        this.buildDiscount();
        this.buildProduct();
    }

    @Override
    public VariantEntity getResult() {
        return this.variantEntity;
    }
}
