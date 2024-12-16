package edu.alexu.cse.dripmeup.component;

import org.springframework.stereotype.Component;

import edu.alexu.cse.dripmeup.dto.ProductSnapshot;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;

@Component
public class ProductMapper {
    public ProductSnapshot toDTO(ProductEntity productEntity) {
        return new ProductSnapshot(productEntity);
    }
}
