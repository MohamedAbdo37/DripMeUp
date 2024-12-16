package edu.alexu.cse.dripmeup.service;

import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.repository.CategoryRepository;
import edu.alexu.cse.dripmeup.service.builder.CategoryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

//    @Autowired
//    private final CategoryBuilder categoryBuilder;

    public CategoryService(CategoryRepository repository) {this.categoryRepository = repository;}

    public void createCategory(String name, String description, Optional<Long> parentID) {
        CategoryEntity category = new CategoryEntity();
        CategoryBuilder builder = new CategoryBuilder(category);
        if (parentID.isEmpty()) {
            builder.build(name, description, Optional.empty());
        }else {
            CategoryEntity parent = this.categoryRepository.findById(parentID.get()).orElse(null);
            builder.build(name, description, Optional.of(parent.getId()));
        }
        this.categoryRepository.save(builder.getResult());

//        CategoryEntity parent = this.categoryRepository.findById(parentID).orElse(null);
//        builder.build(name, description, parent);
//        this.categoryRepository.save(builder.getResult());
    }

    public void deleteCategory(Long categoryId) {
        this.categoryRepository.deleteById(categoryId);
    }

    public List<CategoryEntity> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    public Optional<CategoryEntity> getCategoryById(Long categoryId) {
        return this.categoryRepository.findById(categoryId);
    }

    public CategoryEntity getCategoryByName(String categoryName) {
        return this.categoryRepository.findByName(categoryName);
    }

    public void updateCategory(Long categoryID, Optional<String> name, Optional<String> description) {
        CategoryEntity category = this.categoryRepository.findById(categoryID).orElse(null);
        if (name.isPresent()) {
            category.setName(name.get());
        }
        if (description.isPresent()) {
            category.setDescription(description.get());
        }
        this.categoryRepository.save(category);
    }

    public void addChildren(CategoryEntity parent, CategoryEntity child) {
        parent.addChild(child);
        this.categoryRepository.save(parent);
    }
}
