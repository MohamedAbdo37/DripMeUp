package edu.alexu.cse.dripmeup.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ITEM")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemID;

    @Column(name = "productID")
    private Long productID;

    
    @Column(name = "variantID")
    private Long variantID;

    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "variantID")
    private VariantEntity variant;

    @ManyToOne
    @JoinColumn(name = "productID")
    private ProductEntity product;

    @Column(name = "ProductName")
    private String productName;
    
    @Column(name = "ProductVariantSize")
    private String productVariantSize;
    
    @Column(name = "ProductVariantColor")
    private String productVariantColor;

    @Column(name = "ProductVariantQuantity")
    private Integer productVariantQuantity;

}
