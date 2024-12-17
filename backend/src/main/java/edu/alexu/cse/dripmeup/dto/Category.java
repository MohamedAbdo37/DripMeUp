package edu.alexu.cse.dripmeup.dto;

import edu.alexu.cse.dripmeup.entity.CategoryEntity;

import java.util.List;
import java.util.stream.Collectors;

public class Category {

    private CategoryEntity categoryEntity;
    private String name;
    private String description;
    private List<String> subcategoryNames;

    public Category() {
    }

    public Category(CategoryEntity categoryEntity) {

        this.categoryEntity = categoryEntity;
        this.name = categoryEntity.getName();
        this.description = categoryEntity.getDescription();
        this.subcategoryNames = categoryEntity.getSubcategories().stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSubcategoryNames() {
        return subcategoryNames;
    }

    public void setSubcategoryNames(List<String> subcategoryNames) {
        this.subcategoryNames = subcategoryNames;
    }

    public Long getId() {
        return this.categoryEntity.getId();
    }
}