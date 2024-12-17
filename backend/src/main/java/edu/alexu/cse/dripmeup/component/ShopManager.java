package edu.alexu.cse.dripmeup.component;

import java.util.List;

import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import edu.alexu.cse.dripmeup.dto.Product;
import edu.alexu.cse.dripmeup.dto.ProductSnapshot;
import edu.alexu.cse.dripmeup.dto.Variant;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.repository.ItemRepository;
import edu.alexu.cse.dripmeup.repository.ProductRepository;
import edu.alexu.cse.dripmeup.repository.VariantRepository;
import edu.alexu.cse.dripmeup.service.ProductService;

@Component
public class ShopManager {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private VariantRepository variantRepository;
    
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProductMapper productMapper;
    
    public ProductRepository getProductRepository() {
        return productRepository;
    }
    
    public VariantRepository getVariantRepository() {
        return variantRepository;
    }
    
    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    public Page<ProductSnapshot> getAllProducts(int page, int size) {
        Page<ProductEntity> products = new ProductService().getAllProducts(this.productRepository, page, size);
        return products.map(this.productMapper::toPSDTO);
    }

    public List<Variant> getVariantsOfProduct(ProductEntity product) {
        List<VariantEntity> variants = new ProductService().getVariantsOfProduct(product);
        return variants.stream().map(this.productMapper::toVariantDTO).toList();
    }

    public String getImageOfProduct(ProductEntity product) {
        return new ProductService().getImageOfProduct(product, this.variantRepository);
    }

    public Product getProduct(long productID) {
        return new Product(new ProductService().getProduct(this.productRepository, productID), this);
    }

    public void createProduct(Product product) {
        // TODO Auto-generated method stub
        
    }
}
