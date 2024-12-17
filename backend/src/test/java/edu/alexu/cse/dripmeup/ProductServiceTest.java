package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.dto.Product;
import edu.alexu.cse.dripmeup.dto.Variant;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private VariantRepository variantRepository;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProducts() {
        // Arrange
        when(productRepository.findAll(any(Pageable.class))).return
        // ProductEntity product = new ProductEntity();
        // product.setId(1L);
        // Page<ProductEntity> page = new PageImpl<>(Collections.singletonList(product));
        // when(productRepository.findAll(any(PageRequest.class))).thenReturn(page);

        // // Act
        // Page<ProductEntity> result = productService.getAllProducts(productRepository, 0, 10);

        // // Assert
        // assertNotNull(result);
        // assertEquals(1, result.getContent().size());
        // assertEquals(1L, result.getContent().get(0).getId());
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
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        ProductEntity result = productService.getProduct(productRepository, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateProduct() {
        // Arrange
        Product productDto = new Product(); // Simulate DTO
        ProductEntity productEntity = new ProductEntity();
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        // Act
        ProductEntity result = productService.createProduct(productRepository, productDto);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    public void testCreateVariant() {
        // Arrange
        Variant variantDto = new Variant();
        ProductEntity product = new ProductEntity();
        VariantEntity variantEntity = new VariantEntity();

        when(variantRepository.save(any(VariantEntity.class))).thenReturn(variantEntity);

        // Act
        VariantEntity result = productService.crateVariant(variantRepository, variantDto, product);

        // Assert
        assertNotNull(result);
        verify(variantRepository, times(1)).save(any(VariantEntity.class));
    }

    @Test
    public void testAddImage() {
        // Arrange
        VariantEntity variant = new VariantEntity();
        String imagePath = "image.jpg";

        // ArgumentCaptor<VariantImageEntity> captor = ArgumentCaptor.forClass(VariantImageEntity.class);

        when(imageRepository.save(any(VariantImageEntity.class))).thenReturn(null);

        // Act
        productService.addImage(imageRepository, imagePath, variant);

        // Assert
        assertEquals(imagePath, variant.getVariantImages().get(0).getImagePath());
        // verify(imageRepository.save(any(VariantImageEntity.class)), times(1));
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
