package edu.alexu.cse.dripmeup.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.alexu.cse.dripmeup.CloudinaryUploader;
import edu.alexu.cse.dripmeup.dto.Category;
import edu.alexu.cse.dripmeup.dto.Product;
import edu.alexu.cse.dripmeup.dto.ProductSnapshot;
import edu.alexu.cse.dripmeup.dto.Variant;
import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;
import edu.alexu.cse.dripmeup.exception.ProductCreationException;
import edu.alexu.cse.dripmeup.repository.CategoryRepository;
import edu.alexu.cse.dripmeup.repository.ImageRepository;
//import edu.alexu.cse.dripmeup.repository.ItemRepository;
import edu.alexu.cse.dripmeup.repository.ProductRepository;
import edu.alexu.cse.dripmeup.repository.VariantRepository;
import edu.alexu.cse.dripmeup.service.ProductService;
import edu.alexu.cse.dripmeup.service.ResponseBodyMessage;

@Component
public class ShopManager {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantRepository variantRepository;

//    @Autowired
//    private ItemRepository itemRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CloudinaryUploader cloudinaryUploader;

    @Autowired
    private CategoryManager categoryManager;

    @Autowired
    private CategoryRepository categoryRepository;
    
    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public VariantRepository getVariantRepository() {
        return variantRepository;
    }

//    public ItemRepository getItemRepository() {
//        return itemRepository;
//    }

    public Page<ProductSnapshot> getAllProducts(int page, int size) {
        Page<ProductEntity> products = new ProductService().getAllProducts(this.productRepository, page, size);
        return products.map((product -> this.productMapper.toPSDTO(product, this)));
    }

    public Page<ProductSnapshot> getProductsByCategory(String category,int page, int size) {
        CategoryEntity categoryEntity = this.categoryManager.getCategoryEntityByName(category);
        Page<ProductEntity> products = new ProductService().getProductsByCategory(this.productRepository, categoryEntity, page, size);
        return products.map((product -> this.productMapper.toPSDTO(product, this)));
    }

    public List<Variant> getVariantsOfProduct(ProductEntity product) {
        List<VariantEntity> variants = new ProductService().getVariantsOfProduct(product);
        if (variants == null)
            return List.of();
        return variants.stream().map(variant -> this.productMapper.toVariantDTO(variant, this)).toList();
    }

    public String getImageOfProduct(ProductEntity product) {
        VariantEntity variant = this.minimumPrice(product);
        if(variant == null)
            return "";
        if (variant.getVariantImages().isEmpty())
            return "";
        return variant.getVariantImages().get(0).getImagePath();
    }

    public Product getProduct(long productID) {
        return new Product(new ProductService().getProduct(this.productRepository, productID), this);
    }

    public Product createProduct(Product product) {
        List<CategoryEntity> categoryEntities = new ArrayList<>();

        for(Category c: product.getCategories()){
            CategoryEntity ce = this.categoryManager.getCategoryEntityByName(c.getName());
            if (ce == null)
                throw new RuntimeException("There is no with name " + c.getName());
            categoryEntities.add(ce);
        }
        Product newProduct = new Product(new ProductService().createProduct(this.productRepository, product, categoryEntities), this);
//        System.out.println("product description: " + newProduct.getDescription());

        for(CategoryEntity c: categoryEntities){
            this.categoryRepository.save(c);
        }

        return newProduct;
    }

    public Variant crateVariant(Variant variant, Long productID) throws ProductCreationException {
        ProductEntity product = this.productRepository.findByProductID(productID);
        if (product == null)
            throw new ProductCreationException("There is no product with id " + productID);

        this.productRepository.save(product);
        return new ProductMapper().toVariantDTO(new ProductService().crateVariant(this.variantRepository, variant, product), this);
    }

    public void addImageToVariant(Long variantID, String imagePath) {
        VariantEntity variant = this.variantRepository.findByVariantID(variantID);
        if (variant == null)
            throw new IllegalArgumentException("There is no variant with id " + variantID);
        new ProductService().addImage(this.imageRepository, imagePath, variant);
    }

    public String getImagePath(byte[] image) throws IOException {
        if (!CloudinaryUploader.isValidImage(image)) 
            throw new ProductCreationException("Invalid image");
            
        return this.cloudinaryUploader.uploadImage(image);
    }

    public List<String> getImagesOfVariant(VariantEntity variant) {
        List<VariantImageEntity> images = new ProductService().getImagesOfVariant(variant);
        List<String> paths = new ArrayList<>();
        if (images == null)
            return List.of();
        else {

            for(VariantImageEntity i: images)
                paths.add(i.getImagePath());
            
        }
        return paths;
    }

    private VariantEntity minimumPrice(ProductEntity product){
        return new ProductService().minimumPrice(product);
    }

    public List<Category> getProductCategories(ProductEntity product){
        List<Category> categories = new ArrayList<>();
        for(CategoryEntity c: product.getCategories()){
            categories.add(this.categoryManager.getCategoryByName(c.getName()));
        }
        return categories;
    }

    public double getPriceOfProduct(ProductEntity productEntity) {
        VariantEntity variant = this.minimumPrice(productEntity);
        if(variant == null)
            return 0;
        return variant.getPrice();
    }

    public double getDicountOfProduct(ProductEntity productEntity) {
        VariantEntity variant = this.minimumPrice(productEntity);
        if(variant == null)
            return 0;
        return variant.getDiscount();
    }

}

