package edu.alexu.cse.dripmeup.dto;

import edu.alexu.cse.dripmeup.component.ShopManager;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class VariantTest {

    private VariantEntity variantEntity;
    private ShopManager shopManager;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        shopManager = Mockito.mock(ShopManager.class);

        // Initialize a VariantEntity
        variantEntity = new VariantEntity();
        variantEntity.setVariantID(101L);
        variantEntity.setColor("Red");
        variantEntity.setPrice(150.0);
        variantEntity.setWeight("1.5kg");
        variantEntity.setLength("25cm");
        variantEntity.setSize("M");
        variantEntity.setStock(100);
        variantEntity.setSold(20);
        variantEntity.setState(ProductState.ON_SALE);
        variantEntity.setDiscount(10.0);

        // Mock the ShopManager behavior
        when(shopManager.getImagesOfVariant(variantEntity)).thenReturn(List.of("img1.jpg", "img2.jpg"));
    }

    @Test
    void testVariantConstructor() {
        // Create a Variant DTO using the VariantEntity and ShopManager
        Variant variant = new Variant(variantEntity, shopManager);

        // Verify the fields
        assertNotNull(variant, "Variant object should not be null");
        assertEquals(101L, variant.getVariantID(), "Variant ID should match");
        assertEquals("Red", variant.getColor(), "Color should match");
        assertEquals(150.0, variant.getPrice(), "Price should match");
        assertEquals("1.5kg", variant.getWeight(), "Weight should match");
        assertEquals("25cm", variant.getLength(), "Length should match");
        assertEquals("M", variant.getSize(), "Size should match");
        assertEquals(100, variant.getStock(), "Stock should match");
        assertEquals(20, variant.getSold(), "Sold count should match");
        assertEquals(ProductState.ON_SALE, variant.getState(), "State should match");
        assertEquals(10.0, variant.getDiscount(), "Discount should match");
        assertNotNull(variant.getImages(), "Images list should not be null");
        assertEquals(2, variant.getImages().size(), "Images list size should match");
        assertEquals("img1.jpg", variant.getImages().get(0), "First image should match");
        assertEquals("img2.jpg", variant.getImages().get(1), "Second image should match");
    }
}
