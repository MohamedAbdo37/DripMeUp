package edu.alexu.cse.dripmeup.controller;

import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.service.CategoryService;
import edu.alexu.cse.dripmeup.service.ResponseBodyMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/7/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoryEntity>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createCategory(@RequestParam String categoryName, @RequestParam String categoryDescription, @RequestParam Optional<Long> parentID) {
        try {
            categoryService.createCategory(categoryName, categoryDescription, parentID);
            return ResponseEntity.ok(ResponseBodyMessage.message("Category created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseBodyMessage.error(e.getMessage()));
        }
    }

    @GetMapping("/{categoryName}")
    ResponseEntity<CategoryEntity> getCategoryByName(@PathVariable String categoryName) {
        return ResponseEntity.ok(categoryService.getCategoryByName(categoryName));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteCategory(@RequestParam String categoryName) {
        try {
            CategoryEntity category = categoryService.getCategoryByName(categoryName);
            categoryService.deleteCategory(category.getId());
            return ResponseEntity.ok(ResponseBodyMessage.message("Category deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseBodyMessage.error(e.getMessage()));
        }
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<Map<String, String>> updateCategory(@PathVariable Long id, @RequestParam Optional<String> newCategoryName, @RequestParam Optional<String> newCategoryDescription) {
        try {
            categoryService.updateCategory(id, newCategoryName, newCategoryDescription);
            return ResponseEntity.ok(ResponseBodyMessage.message("Category updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseBodyMessage.error(e.getMessage()));
        }
    }


//    @GetMapping("/{id}")
//    public ResponseEntity<CategoryEntity> getCategoryById(@PathVariable Long id) {
//        return ResponseEntity.ok(categoryService.getCategoryById(id));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Map<String, String>> updateCategory(@PathVariable Long id, @RequestBody CategoryEntity category) {
//        try {
//            categoryService.updateCategory(id, category);
//            return ResponseEntity.ok(ResponseBodyMessage.message("Category updated successfully"));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(ResponseBodyMessage.error(e.getMessage()));
//        }
//    }
//
//    @DeleteMapping("/category/{id}")
//    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long id) {
//        try {
//            categoryService.deleteCategory(id);
//            return ResponseEntity.ok(ResponseBodyMessage.message("Category deleted successfully"));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(ResponseBodyMessage.error(e.getMessage()));
//        }
//    }
}
