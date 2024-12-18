package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.dto.Product;
import edu.alexu.cse.dripmeup.dto.Variant;
import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import edu.alexu.cse.dripmeup.repository.ImageRepository;
import edu.alexu.cse.dripmeup.repository.ProductRepository;
import edu.alexu.cse.dripmeup.repository.VariantRepository;
import edu.alexu.cse.dripmeup.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import edu.alexu.cse.dripmeup.exception.EODException;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private VariantRepository variantRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private CategoryEntity categoryEntity;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProducts() {
        // Arrange
        ProductEntity product = new ProductEntity();
        product.setProductID(1L);
        Page<ProductEntity> page = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(page);

        // Act
        Page<ProductEntity> result = productService.getAllProducts(productRepository, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getProductID());
        verify(productRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    public void testGetImageOfProduct() {
        // Arrange
        ProductEntity product = new ProductEntity();
        VariantEntity variant = new VariantEntity();
        VariantImageEntity image = new VariantImageEntity();
        image.setImagePath("image_path.jpg");

        variant.setVariantImages(Collections.singletonList(image));
        when(variantRepository.findByProduct(product)).thenReturn(Collections.singletonList(variant));

        // Act
        String imagePath = productService.getImageOfProduct(product, variantRepository);

        // Assert
        assertEquals("image_path.jpg", imagePath);
        verify(variantRepository, times(1)).findByProduct(product);
    }

    @Test
    public void testGetImageOfProductThrowsException() {
        // Arrange
        ProductEntity product = new ProductEntity();
        VariantEntity variant = new VariantEntity();
        VariantImageEntity image = new VariantImageEntity();
        image.setImagePath("image_path.jpg");

        variant.setVariantImages(Collections.singletonList(image));
        when(variantRepository.findByProduct(product)).thenReturn(List.of());

        assertThrows(EODException.class,()-> productService.getImageOfProduct(product, variantRepository));
    }

    @Test
    public void testGetVariantsOfProduct() {
        // Arrange
        ProductEntity product = new ProductEntity();
        VariantEntity variant = new VariantEntity();
        product.setVariants(Collections.singletonList(variant));

        // Act
        List<VariantEntity> result = productService.getVariantsOfProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verifyNoInteractions(variantRepository, productRepository); // No DB interaction
    }

    @Test
    public void testGetProduct() {
        // Arrange
        ProductEntity product = new ProductEntity();
        product.setProductID(1L);
        when(productRepository.findByProductID(1L)).thenReturn(product);

        // Act
        ProductEntity result = productService.getProduct(productRepository, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getProductID());
        verify(productRepository, times(1)).findByProductID(1L);
    }


    @Test
    public void testCreateProduct() {
        CategoryEntity category = new CategoryEntity();
        // Arrange
        Product productDto = new Product();
        productDto = new Product();
        productDto.setDescription("Test Product");
        productDto.setDateOfCreation("2022-11-21 00:00");
        productDto.setState(ProductState.ON_SALE); // Simulate DTO
        when(productRepository.save(any(ProductEntity.class))).thenReturn(null);
        // when(categoryEntity.addProduct(any(ProductEntity.class))).thenReturn(null);
        // Act
        ProductEntity result = productService.createProduct(productRepository, productDto,List.of(category));
        assertNotNull(result);
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    public void testGetImagesOfVariant() {
        // Arrange
        VariantImageEntity image = new VariantImageEntity();
        VariantEntity variant = new VariantEntity();
        variant.setVariantImages(Collections.singletonList(image));

        // Act
        List<VariantImageEntity> result = productService.getImagesOfVariant(variant);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verifyNoInteractions(imageRepository); // No DB interaction
    }

    
}
