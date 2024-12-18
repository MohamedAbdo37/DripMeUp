package edu.alexu.cse.dripmeup.repository;

import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    void deleteById(Long categoryId);
    List<CategoryEntity> findAll();
    Optional<CategoryEntity> findById(Long categoryId);
    CategoryEntity findByName(String categoryName);
}
