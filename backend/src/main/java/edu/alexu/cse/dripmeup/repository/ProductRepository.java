package edu.alexu.cse.dripmeup.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import java.util.List;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;



public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @SuppressWarnings("null")
    @Override
    Page<ProductEntity> findAll(Pageable pageable);

    Page<ProductEntity> findByCategories(CategoryEntity category, Pageable pageable);

    ProductEntity findByProductID(long productID);
    
}
