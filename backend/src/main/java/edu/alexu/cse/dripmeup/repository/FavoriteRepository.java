package edu.alexu.cse.dripmeup.repository;
import edu.alexu.cse.dripmeup.entity.FavoriteEntity;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository  extends JpaRepository<FavoriteEntity, Long> {
    FavoriteEntity save(FavoriteEntity favorite) ;

    FavoriteEntity findByUserAndVariant(UserEntity user , VariantEntity variant) ;

    List<FavoriteEntity> findAllByUserOrderByTimeDesc(UserEntity user) ;

    @Transactional
    Long deleteByUserAndVariant(UserEntity user , VariantEntity variant);

    @Transactional
    Long deleteAllByUser(UserEntity user) ;
}
