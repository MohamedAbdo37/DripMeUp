package edu.alexu.cse.dripmeup.repository;

import edu.alexu.cse.dripmeup.entity.CartEntity;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    CartEntity save(CartEntity cart) ;

    CartEntity findByUserAndVariant(UserEntity user , VariantEntity variant) ;

    List<CartEntity> findAllByUserOrderByTimeDesc(UserEntity user) ;

    List<CartEntity> findAllByUser(UserEntity user) ;

    @Transactional
    Long deleteByUserAndVariant(UserEntity user , VariantEntity variant);

    @Transactional
    Long deleteAllByUser(UserEntity user) ;

}
