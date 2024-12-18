package edu.alexu.cse.dripmeup.controller;

import edu.alexu.cse.dripmeup.component.CategoryManager;
import edu.alexu.cse.dripmeup.dto.Category;
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

    @PostMapping("/random")
    public ResponseEntity<?> randomCategories() {
        try {
            // Create Parent Categories
            categoryManager.createCategory("Men", "Men's clothing", Optional.empty());
            categoryManager.createCategory("Women", "Women's clothing", Optional.empty());
            categoryManager.createCategory("Children", "Children's clothing", Optional.empty());
            
            Long menId = categoryManager.getCategoryByName("Men").getId();
            Long womenId = categoryManager.getCategoryByName("Women").getId();
            Long childrenId = categoryManager.getCategoryByName("Children").getId();

            // Define Subcategories
            List<Map<String, Object>> subcategories = List.of(
                Map.of("name", "Shirts", "description", "Casual and formal shirts", "parentCategoryid", menId),
                Map.of("name", "Pants", "description", "Trousers and jeans", "parentCategoryid", menId),
                Map.of("name", "Suits", "description", "Formal suits", "parentId", menId),
                Map.of("name", "Tops", "description", "Blouses and casual tops", "parentId", 1),
                Map.of("name", "Dresses", "description", "Formal and casual dresses", "parentId", womenId),
                Map.of("name", "Skirts", "description", "Different styles of skirts", "parentId", womenId),
                Map.of("name", "T-shirts", "description", "Casual T-shirts", "parentId", childrenId),
                Map.of("name", "Shorts", "description", "Summer wear shorts", "parentId", childrenId),
                Map.of("name", "Outerwear", "description", "Jackets and coats", "parentId", childrenId),
                Map.of("name", "Sleepwear", "description", "Nightwear for kids", "parentId", childrenId)
            );
            System.out.println(menId);
            // Create Subcategories
            for (Map<String, Object> subcategory : subcategories) {
                String name = (String) subcategory.get("name");
                String description = (String) subcategory.get("description");
                Long parentId = (Long) subcategory.get("parentId");
                categoryManager.createCategory(name, description, Optional.of(parentId));
                categoryManager.getCategoryByName(name).getId();
            }
    
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok("done");
    }
}
