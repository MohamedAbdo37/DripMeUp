package edu.alexu.cse.dripmeup.service;

import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;
import edu.alexu.cse.dripmeup.repository.ImageRepository;
import edu.alexu.cse.dripmeup.repository.ProductRepository;
import edu.alexu.cse.dripmeup.repository.VariantRepository;
import edu.alexu.cse.dripmeup.service.builder.ProductBuilder;
import edu.alexu.cse.dripmeup.service.builder.ProductBuilderIF;
import edu.alexu.cse.dripmeup.service.builder.VariantBuilder;
import edu.alexu.cse.dripmeup.service.builder.VariantImageBuilder;

public class ProductDirector {

    private ProductRepository productRepository;
    private VariantRepository variantRepository;
    // private ItemRepository itemRepository;
    private ImageRepository imageRepository;

    private ProductBuilderIF builder;

    public ProductDirector(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDirector(VariantRepository variantRepository) {
        this.variantRepository = variantRepository;
    }

    public ProductDirector(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    // public ProductDirector(ItemRepository itemRepository) {
    //     this.productRepository = productRepository;
    // }
    
    public void construct(ProductBuilderIF builder) {
        this.builder = builder;
        this.builder.build();
    }

    // public void edit(ProductBuilderIF builder) {
    //     if(builder instanceof ProductBuilder) {
            
    //     }
    // }

    public ProductEntity getProduct(){
        ProductBuilder productBuilder = (ProductBuilder) this.builder;
        ProductEntity product = productBuilder.getResult();
        this.productRepository.save(product);
        return product;
    }

    public VariantEntity getVariant(){
        VariantBuilder variantBuilder = (VariantBuilder) this.builder;
        VariantEntity variant = variantBuilder.getResult();
        this.variantRepository.save(variant);
        return variant;
    }

    public VariantImageEntity getImage(){
        VariantImageBuilder imageBuilder = (VariantImageBuilder) this.builder;
        VariantImageEntity image = imageBuilder.getResult();
        this.imageRepository.save(image);
        return image;
    }
}
