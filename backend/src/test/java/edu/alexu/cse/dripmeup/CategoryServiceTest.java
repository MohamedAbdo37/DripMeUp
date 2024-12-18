package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.repository.CategoryRepository;
import edu.alexu.cse.dripmeup.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCategory_WithParent() {
        String name = "Electronics";
        String description = "Electronic items";
        Long parentID = 1L;

        CategoryEntity parentCategory = new CategoryEntity();
        parentCategory.setId(parentID);

        when(categoryRepository.findById(parentID)).thenReturn(Optional.of(parentCategory));

        categoryService.createCategory(name, description, Optional.of(parentID));

        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    public void testCreateCategory_WithoutParent() {
        String name = "Books";
        String description = "All kinds of books";

        categoryService.createCategory(name, description, Optional.empty());

        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    public void testDeleteCategory() {
        Long categoryId = 1L;

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    public void testGetAllCategories() {
        CategoryEntity category1 = new CategoryEntity();
        CategoryEntity category2 = new CategoryEntity();

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<CategoryEntity> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testGetCategoryById() {
        Long categoryId = 1L;
        CategoryEntity category = new CategoryEntity();
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Optional<CategoryEntity> result = categoryService.getCategoryById(categoryId);

        assertTrue(result.isPresent());
        assertEquals(categoryId, result.get().getId());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void testGetCategoryByName() {
        String categoryName = "Men Casuals";
        CategoryEntity category = new CategoryEntity();
        category.setName(categoryName);

        when(categoryRepository.findByName(categoryName)).thenReturn(category);

        CategoryEntity result = categoryService.getCategoryByName(categoryName);

        assertNotNull(result);
        assertEquals(categoryName, result.getName());
        verify(categoryRepository, times(1)).findByName(categoryName);
    }

    @Test
    public void testUpdateCategory() {
        Long categoryId = 1L;
        String newName = "Updated Name";
        String newDescription = "Updated Description";
        CategoryEntity category = new CategoryEntity();
        category.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.updateCategory(categoryId, Optional.of(newName), Optional.of(newDescription));

        assertEquals(newName, category.getName());
        assertEquals(newDescription, category.getDescription());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void testAddChildren() {
        Long parentId = 1L;
        CategoryEntity parent = new CategoryEntity();
        parent.setId(parentId);
        CategoryEntity child = new CategoryEntity();

        when(categoryRepository.findById(parentId)).thenReturn(Optional.of(parent));

        categoryService.addChildren(parent, child);

        verify(categoryRepository, times(1)).save(parent);
    }

    @Test
    public void testAddChildren_ParentNotFound() {
        Long parentId = 1L;
        CategoryEntity parent = new CategoryEntity();
        parent.setId(parentId);
        CategoryEntity child = new CategoryEntity();

        when(categoryRepository.findById(parentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            categoryService.addChildren(parent, child);
        });

        String expectedMessage = "Parent category not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}