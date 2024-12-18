package edu.alexu.cse.dripmeup.entity.product;

import java.util.List;
import java.util.Map;

import edu.alexu.cse.dripmeup.enumeration.ProductState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "VARIANT")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class VariantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "variantID")
    private Long variantID;

    @ManyToOne
    @JoinColumn(name = "productID")
    private ProductEntity product ;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
    private List<VariantImageEntity> variantImages ;

    @OneToMany
    private List<ItemEntity> items ;

    @Column(name = "Color")
    private String color ;

    @Column(name = "Weight")
    private String weight ;

    @Column(name = "Length")
    private String length ;

    @Column(name = "Size")
    private String size ;

    @Column(name = "Price")
    private double price ;

    @Column(name = "Stock")
    private int stock ;

    @Column(name = "Sold")
    private int sold ;

    @Column(name = "State")
    private ProductState state ;

    public int getStock() {
        return this.stock ;
    }

    public Long getVariantID() {
        return this.variantID ;
    }

    public String getColor() {
        return this.color ;
    }

    public String getWeight() {
        return this.weight ;
    }

    public String getLength() {
        return this.length ;
    }

    public String getSize() {
        return this.size ;
    }

    public int getSold() {
        return this.sold ;
    }

    public ProductState getState() {
        return this.state ;
    }

    public ProductEntity getProduct(){
        return this.product ;
    }

    public List<VariantImageEntity> getVariantImages(){
        return this.variantImages ;
    }

    public double getPrice(){
        return this.price ;
    }

    public void setVariantID(Long variantID) {
        this.variantID = variantID;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public void setVariantImages(List<VariantImageEntity> variantImages) {
        this.variantImages = variantImages;
    }
}
