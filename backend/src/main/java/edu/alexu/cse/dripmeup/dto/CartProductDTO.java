package edu.alexu.cse.dripmeup.dto;

import edu.alexu.cse.dripmeup.enumeration.ProductState;
import java.util.List;

public class CartProductDTO {
    private Long variantId ;
    private Long productId ;
    private String description ;
    private String color ;
    private String length ;
    private String weight ;
    private String size ;
    private double price ;
    private ProductState state ;
    private int stock ;
    List<String> images ;


    public CartProductDTO(Long variantId, Long productId , String description, String color, String length, String weight,
                          String size, double price, ProductState state, int stock, List<String> images) {

        this.variantId = variantId;
        this.productId = productId ;
        this.description = description;
        this.color = color;
        this.length = length;
        this.weight = weight;
        this.size = size;
        this.price = price;
        this.state = state;
        this.stock = stock;
        this.images = images;
    }

    public Long getVariantId() {
        return this.variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLength() {
        return this.length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductState getState() {
        return this.state;
    }

    public void setState(ProductState state) {
        this.state = state;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}
