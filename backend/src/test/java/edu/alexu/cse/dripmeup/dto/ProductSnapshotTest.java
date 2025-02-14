package edu.alexu.cse.dripmeup.dto;

import edu.alexu.cse.dripmeup.component.ShopManager;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ProductSnapshotTest {

    @Test
    void testProductSnapshotCreation() {
        // Mock ProductEntity
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductID(1L);
        productEntity.setState(ProductState.ON_SALE);
        productEntity.setDescription("Test Product");
        productEntity.setTime(LocalDateTime.of(2024, 5, 20, 10, 30));

        // Mock ShopManager
        ShopManager shop = mock(ShopManager.class);

        // Create ProductSnapshot
        ProductSnapshot snapshot = new ProductSnapshot(productEntity, shop);

        assertEquals(1L, snapshot.getProductID(), "Product ID should match");
        assertEquals(0, snapshot.getPrice(), "Default price should be 100");
        assertEquals(ProductState.ON_SALE, snapshot.getState(), "Product state should match");
        assertEquals("Test Product", snapshot.getDescription(), "Description should match");
        assertEquals("2024-05-20T10:30", snapshot.getDateOfCreation(), "Date of creation should match");
        assertEquals("", snapshot.getProductImage(), "Product image should be empty");
    }
}
