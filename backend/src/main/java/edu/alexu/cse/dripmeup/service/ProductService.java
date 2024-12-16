package edu.alexu.cse.dripmeup.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.repository.ProductRepository;
import edu.alexu.cse.dripmeup.repository.VariantRepository;

@Service
public class ProductService {

    public Page<ProductEntity> getAllProducts(ProductRepository productRepository, int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // page starts from 0
        return productRepository.findAll(pageable);
    }

    public String getImageOfProduct(ProductEntity product, VariantRepository variantRepository) {
        List<VariantEntity> variants = variantRepository.findByProduct(product);
        return variants.get(0).getVariantImages().get(0).getImagePath();
    }

    public List<VariantEntity> getVariantsOfProduct(ProductEntity product) {
        return product.getVariants();
    }

    public ProductEntity getProduct(ProductRepository productRepository,long productID) {
        return productRepository.findById(productID).get();
    }
}
