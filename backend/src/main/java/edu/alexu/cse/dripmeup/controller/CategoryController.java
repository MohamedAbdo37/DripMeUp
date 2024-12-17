package edu.alexu.cse.dripmeup.controller;

import edu.alexu.cse.dripmeup.dto.Category;
import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.manager.CategoryManager;
import edu.alexu.cse.dripmeup.service.CategoryService;
import edu.alexu.cse.dripmeup.service.ResponseBodyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/7/categories")
public class CategoryController {

    @Autowired
    private CategoryManager categoryManager;

    @GetMapping("/")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryManager.getAllCategories());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createCategory(@RequestParam String categoryName, @RequestParam String categoryDescription, @RequestParam Optional<Long> parentID) {
        try {
            categoryManager.createCategory(categoryName, categoryDescription, parentID);
            return ResponseEntity.ok(ResponseBodyMessage.message("Category created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseBodyMessage.error(e.getMessage()));
        }
    }

    @GetMapping("/{categoryName}")
    ResponseEntity<Category> getCategoryByName(@PathVariable String categoryName) {
        return ResponseEntity.ok(categoryManager.getCategoryByName(categoryName));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteCategory(@RequestParam String categoryName) {
        try {
            Category category = categoryManager.getCategoryByName(categoryName);
            categoryManager.deleteCategory(category.getId());
            return ResponseEntity.ok(ResponseBodyMessage.message("Category deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseBodyMessage.error(e.getMessage()));
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<Map<String, String>> updateCategory(@PathVariable Long id, @RequestParam Optional<String> newCategoryName, @RequestParam Optional<String> newCategoryDescription) {
        try {
            categoryManager.updateCategory(id, newCategoryName, newCategoryDescription);
            return ResponseEntity.ok(ResponseBodyMessage.message("Category updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseBodyMessage.error(e.getMessage()));
        }
    }
}
