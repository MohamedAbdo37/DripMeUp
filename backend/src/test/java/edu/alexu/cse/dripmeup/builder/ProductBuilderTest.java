//package edu.alexu.cse.dripmeup.builder;
//
//import edu.alexu.cse.dripmeup.dto.Product;
//import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
//import edu.alexu.cse.dripmeup.service.builder.ProductBuilder;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import edu.alexu.cse.dripmeup.enumeration.ProductState;
//import edu.alexu.cse.dripmeup.service.ProductService;
//
//public class ProductBuilderTest {
//
//    private Product product;
//    private ProductBuilder productBuilder;
//    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//
//    @BeforeEach
//    public void setUp() {
//        // Initialize a Product DTO
//        product = new Product();
//        product.setDescription("Test Product");
//        product.setDateOfCreation("2022-11-21 00:00");
//        product.setState(ProductState.ON_SALE);
//
//        // Initialize the ProductBuilder with the Product
//        productBuilder = new ProductBuilder(product);
//    }
//
//    @Test
//    public void testBuildDescription() {
//        // Build the description
//        productBuilder.buildDescription();
//
//        // Verify that the description was set in ProductEntity
//        ProductEntity result = productBuilder.getResult();
//        assertEquals("Test Product", result.getDescription(), "Description should match");
//    }
//
//    @Test
//    public void testBuildTime() {
//        // Build the time
//        productBuilder.buildTime();
//
//        // Verify that the time was set correctly
//        ProductEntity result = productBuilder.getResult();
//        LocalDateTime expectedTime = LocalDateTime.parse("2022-11-21 00:00", formatter);
//        assertEquals(expectedTime, result.getTime(), "Time should match the parsed LocalDateTime");
//    }
//
//    @Test
//    public void testBuildState() {
//        // Build the state
//        productBuilder.buildState();
//
//        // Verify that the state was set in ProductEntity
//        ProductEntity result = productBuilder.getResult();
//        assertEquals(ProductState.ON_SALE, result.getState(), "State should match");
//    }
//
//    @Test
//    public void testBuildAll() {
//        // Build all fields
//        productBuilder.build();
//
//        // Verify all fields in ProductEntity
//        ProductEntity result = productBuilder.getResult();
//        assertNotNull(result, "ProductEntity should not be null");
//
//        assertEquals("Test Product", result.getDescription(), "Description should match");
//        LocalDateTime expectedTime = LocalDateTime.parse("2022-11-21 00:00", formatter);
//        assertEquals(expectedTime, result.getTime(), "Time should match the parsed LocalDateTime");
//        assertEquals(ProductState.ON_SALE, result.getState(), "State should match");
//    }
//}
