package edu.alexu.cse.dripmeup.repository;



import java.util.List;

import edu.alexu.cse.dripmeup.entity.product.VariantEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.alexu.cse.dripmeup.entity.product.ProductEntity;

public interface VariantRepository extends JpaRepository<VariantEntity, Long> {
    List<VariantEntity> findByProduct(ProductEntity product);

    VariantEntity findByVariantID(Long variantID);

}
