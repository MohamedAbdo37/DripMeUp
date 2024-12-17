package edu.alexu.cse.dripmeup.component;

import org.springframework.stereotype.Component;

import edu.alexu.cse.dripmeup.dto.ProductSnapshot;
import edu.alexu.cse.dripmeup.dto.Variant;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;

@Component
public class ProductMapper {
    public ProductSnapshot toPSDTO(ProductEntity productEntity) {
        return new ProductSnapshot(productEntity);
    }

    public Variant toVariantDTO(VariantEntity variantEntity) {
        return new Variant(variantEntity);
    }
}
