// src/test/java/edu/alexu/cse/dripmeup/dto/CategoryDTOTest.java
package edu.alexu.cse.dripmeup.dto;

import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryTest {

    @Test
    public void testMappingWithValidData() {
        // Arrange
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("Test Category");
        categoryEntity.setDescription("Test Description");

        // Act
        Category categoryDTO = new Category(categoryEntity);

        // Assert
        assertNotNull(categoryDTO);
        assertEquals("Test Category", categoryDTO.getName());
        assertEquals("Test Description", categoryDTO.getDescription());
        assertEquals(0, categoryDTO.getSubcategoryNames().size());
    }

    @Test
    public void testMappingWithNullDescription() {
        // Arrange
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("Test Category");
        categoryEntity.setDescription(null);

        // Act
        Category categoryDTO = new Category(categoryEntity);

        // Assert
        assertNotNull(categoryDTO);
        assertEquals("Test Category", categoryDTO.getName());
        assertEquals(null, categoryDTO.getDescription());
        assertEquals(0, categoryDTO.getSubcategoryNames().size());
    }

    @Test
    public void testMappingWithEmptyName() {
        // Arrange
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("");
        categoryEntity.setDescription("Test Description");

        // Act
        Category categoryDTO = new Category(categoryEntity);

        // Assert
        assertNotNull(categoryDTO);
        assertEquals("", categoryDTO.getName());
        assertEquals("Test Description", categoryDTO.getDescription());
        assertEquals(0, categoryDTO.getSubcategoryNames().size());
    }
}