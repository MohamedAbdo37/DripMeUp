package edu.alexu.cse.dripmeup.dto;

import edu.alexu.cse.dripmeup.component.ShopManager;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class ProductTest {

    private ProductEntity productEntity;
    private ShopManager shopManager;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        shopManager = Mockito.mock(ShopManager.class);

        // Initialize a ProductEntity
        productEntity = new ProductEntity();
        productEntity.setProductID(33L);
        productEntity.setState(ProductState.ON_SALE);
        productEntity.setDescription("Test Product Description");
        productEntity.setTime(LocalDateTime.of(2022, 11, 21, 0, 0));

        // Mock the ShopManager behavior
        when(shopManager.getVariantsOfProduct(productEntity)).thenReturn(Collections.emptyList());
    }

    @Test
    void testProductConstructor() {
        // Create a Product DTO using the ProductEntity and ShopManager
        Product product = new Product(productEntity, shopManager);

        // Verify the fields
        assertNotNull(product, "Product object should not be null");
        assertEquals(33L, product.getProductID(), "Product ID should match");
        assertEquals(ProductState.ON_SALE, product.getState(), "Product state should match");
        assertEquals("Test Product Description", product.getDescription(), "Description should match");
        assertEquals("2022-11-21 00:00", product.getDateOfCreation(), "Date of creation should match");
        assertNotNull(product.getVariants(), "Variants list should not be null");
        assertEquals(0, product.getVariants().size(), "Variants list should be empty");
        assertEquals(5.0, product.getRate(), "Rate should default to 5.0");
        assertEquals(1, product.getNumberOfFeedback(), "Number of feedback should default to 1");
    }
}
