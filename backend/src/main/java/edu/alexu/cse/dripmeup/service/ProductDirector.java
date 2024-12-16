package edu.alexu.cse.dripmeup.service;

import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.service.builder.ProductBuilder;

public class ProductDirector {
    public ProductEntity construct(ProductBuilder productBuilder) {
        productBuilder.build();
        return productBuilder.getResult();
    }
}
