// src/main/java/edu/alexu/cse/dripmeup/manager/CategoryManager.java
package edu.alexu.cse.dripmeup.component;

import edu.alexu.cse.dripmeup.dto.Category;
import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.repository.CategoryRepository;
import edu.alexu.cse.dripmeup.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoryManager {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryService.getAllCategories().stream()
                .map(Category::new)
                .collect(Collectors.toList());
    }

    public Category getCategoryByName(String categoryName) {
        CategoryEntity categoryEntity = categoryService.getCategoryByName(categoryName);
        return new Category(categoryEntity);
    }

    public CategoryEntity getCategoryEntityByName(String categoryName) {
        return categoryService.getCategoryByName(categoryName);
    }

    public void createCategory(String name, String description, Optional<Long> parentID) {
        categoryService.createCategory(name, description, parentID);
    }

    public void deleteCategory(Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    public void updateCategory(Long categoryId, Optional<String> name, Optional<String> description) {
        categoryService.updateCategory(categoryId, name, description);
    }
}