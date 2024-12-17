package edu.alexu.cse.dripmeup.builder;

import edu.alexu.cse.dripmeup.dto.Variant;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import edu.alexu.cse.dripmeup.service.builder.VariantBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VariantBuilderTest {

    private Variant variant;
    private ProductEntity product;
    private VariantBuilder builder;

    @BeforeEach
    void setUp() {
        // Initialize the test data
        variant = new Variant();
        variant.setPrice(100.0);
        variant.setColor("Red");
        variant.setWeight("1.2kg");
        variant.setLength("20cm");
        variant.setSize("M");
        variant.setStock(50);
        variant.setDiscount(10.0);

        product = new ProductEntity();
        product.setVariants(new ArrayList<>()); // Empty variants list

        // Instantiate the builder
        builder = new VariantBuilder(variant, product);
    }

    @Test
    void testBuildVariantEntity() {
        builder.build();

        VariantEntity result = builder.getResult();

        assertNotNull(result, "VariantEntity should not be null");
        assertEquals(100.0, result.getPrice(), "Price should match");
        assertEquals("Red", result.getColor(), "Color should match");
        assertEquals("1.2kg", result.getWeight(), "Weight should match");
        assertEquals("20cm", result.getLength(), "Length should match");
        assertEquals("M", result.getSize(), "Size should match");
        assertEquals(50, result.getStock(), "Stock should match");
        assertEquals(0, result.getSold(), "Sold should be initialized to 0");
        assertEquals(ProductState.ON_SALE, result.getState(), "State should be ON_SALE");
        assertEquals(10.0, result.getDiscount(), "Discount should match");
        assertEquals(product, result.getProduct(), "Product reference should match");
    }

    @Test
    void testProductVariantListUpdated() {
        builder.build();

        VariantEntity result = builder.getResult();

        assertTrue(product.getVariants().contains(result), "Product's variant list should include the new VariantEntity");
    }
}
