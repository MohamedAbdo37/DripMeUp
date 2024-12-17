package edu.alexu.cse.dripmeup.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.alexu.cse.dripmeup.entity.product.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    
    Page<ProductEntity> findAll(Pageable pageable);
}
