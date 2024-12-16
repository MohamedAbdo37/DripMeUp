package edu.alexu.cse.dripmeup.repository;



import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<VariantImageEntity, Long> {

    VariantImageEntity save(VariantImageEntity image) ;
}
