// src/test/java/edu/alexu/cse/dripmeup/builder/CategoryBuilderTest.java
package edu.alexu.cse.dripmeup.builder;

import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.service.builder.CategoryBuilder;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryBuilderTest {

    @Test
    public void testBuildCategoryWithValidData() {
        // Arrange
        CategoryEntity categoryEntity = new CategoryEntity();

        CategoryBuilder builder = new CategoryBuilder(categoryEntity);

        // Act
        builder.build("Test Category", "Test Description", Optional.empty());
        CategoryEntity result = builder.getResult();

        // Assert
        assertNotNull(result);
        assertEquals("Test Category", result.getName());
        assertEquals("Test Description", result.getDescription());
    }

    @Test
    public void testBuildCategoryWithNullDescription() {
        // Arrange
        CategoryEntity categoryEntity = new CategoryEntity();

        CategoryBuilder builder = new CategoryBuilder(categoryEntity);

        // Act
        builder.build("Test Category", null, Optional.empty());
        CategoryEntity result = builder.getResult();

        // Assert
        assertNotNull(result);
        assertEquals("Test Category", result.getName());
        assertEquals(null, result.getDescription());
    }

    @Test
    public void testBuildCategoryWithEmptyName() {
        // Arrange
        CategoryEntity categoryEntity = new CategoryEntity();

        CategoryBuilder builder = new CategoryBuilder(categoryEntity);

        // Act
        builder.build("", "Test Description", Optional.empty());
        CategoryEntity result = builder.getResult();

        // Assert
        assertNotNull(result);
        assertEquals("", result.getName());
        assertEquals("Test Description", result.getDescription());
    }
}