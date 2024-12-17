package edu.alexu.cse.dripmeup.service.builder;

import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;

public class VariantImageBuilder implements ProductBuilderIF{

    private final VariantImageEntity variantImage;
    private String path;
    private VariantEntity variant;

    public VariantImageBuilder(String path, VariantEntity variant) {
        this.path = path;
        this.variantImage = new VariantImageEntity();
        this.variant = variant;
    }

    public VariantImageBuilder(VariantImageEntity variantImage) {
        this.variantImage = variantImage;
    }

    public void buildPath(){
        this.variantImage.setImagePath(this.path);
    }

    public void buildVariant(){
        this.variantImage.setVariant(this.variant);
    }

    @Override
    public void build() {
        this.buildPath();
        this.buildVariant();
    }

    @Override
    public VariantImageEntity getResult() {
        return this.variantImage;
    }
    
}
