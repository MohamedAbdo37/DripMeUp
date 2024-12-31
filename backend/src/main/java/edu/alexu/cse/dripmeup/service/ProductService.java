package edu.alexu.cse.dripmeup.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.alexu.cse.dripmeup.dto.Product;
import edu.alexu.cse.dripmeup.dto.Variant;
import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;
import edu.alexu.cse.dripmeup.exception.EODException;
import edu.alexu.cse.dripmeup.repository.ImageRepository;
import edu.alexu.cse.dripmeup.repository.ProductRepository;
import edu.alexu.cse.dripmeup.repository.VariantRepository;
import edu.alexu.cse.dripmeup.service.builder.ProductBuilder;
import edu.alexu.cse.dripmeup.service.builder.VariantBuilder;
import edu.alexu.cse.dripmeup.service.builder.VariantImageBuilder;

@Service
public class ProductService {

    public Page<ProductEntity> getAllProducts(ProductRepository productRepository, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    public String getImageOfProduct(ProductEntity product, VariantRepository variantRepository) {
        List<VariantEntity> variants = variantRepository.findByProduct(product);
        if(variants == null || variants.isEmpty())
            throw new EODException("there is no data available");
        return variants.get(0).getVariantImages().get(0).getImagePath();
    }


    public ProductEntity getProduct(ProductRepository productRepository,long productID) {
        return productRepository.findByProductID(productID);
    }

    public ProductEntity createProduct(ProductRepository productRepository, Product product, List<CategoryEntity> categories) {
        ProductDirector director = new ProductDirector(productRepository);
        director.construct(new ProductBuilder(product, categories));
        return director.getProduct();
    }

    public VariantEntity crateVariant(VariantRepository variantRepository, Variant variant, ProductEntity product) {
        ProductDirector director = new ProductDirector(variantRepository);
        director.construct(new VariantBuilder(variant, product));
        return director.getVariant();
    }

    public void addImage(ImageRepository imageRepository, String path, VariantEntity variant) {
        ProductDirector director = new ProductDirector(imageRepository);
        director.construct(new VariantImageBuilder(path, variant));
        director.getImage();
    }

    public List<VariantImageEntity> getImagesOfVariant(VariantEntity variant) {
        return variant.getVariantImages();
    }

    public List<VariantEntity> getVariantsOfProduct(ProductEntity product, VariantRepository variantRepository) {
        return variantRepository.findByProduct(product);
    }

    public VariantEntity minimumPrice(ProductEntity product, VariantRepository variantRepository) {
        VariantEntity variantEntity = null;
        double minimumPrice = Double.MAX_VALUE;

        for(VariantEntity v: variantRepository.findByProduct(product)){
            double value = v.getPrice() * v.getDiscount();
            if(variantEntity == null || value < minimumPrice){
                variantEntity = v;
                minimumPrice = value;
            }
        }
        return variantEntity;
    }

    public Page<ProductEntity> getProductsByCategory(ProductRepository productRepository, CategoryEntity categoryEntity,
            int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByCategories(categoryEntity, pageable);
    }

}
