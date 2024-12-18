package edu.alexu.cse.dripmeup.service.builder;
import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class CategoryBuilder {

    private final CategoryEntity category;

    public CategoryBuilder(CategoryEntity category) {
        this.category = category;
    }

    public void build(String name, String description, Optional<Long> parentID) {
        this.category.setName(name);
        this.category.setDescription(description);
        if (parentID.isPresent()) {
            this.category.setParentId(parentID);
        }
    }

    public CategoryEntity getResult() {
        return this.category;
    }

}
