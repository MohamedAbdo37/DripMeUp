package edu.alexu.cse.dripmeup.entity;

import edu.alexu.cse.dripmeup.dto.Variant;
import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;
import edu.alexu.cse.dripmeup.repository.ItemImageRepository;
import edu.alexu.cse.dripmeup.repository.ItemRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;




@Entity
@Table(name = "ITEM")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ItemId")
    private Long itemId;

    @Column(name = "VariantId")
    private Long variantId;

    @Column(name = "ProductId")
    private Long productId;

    @Column(name = "ProductVariantSize")
    private String productVariantSize;

    @Column(name = "ProductVariantColor")
    private String productVariantColor;

    @Column(name = "ProductVariantWeight")
    private String productVariantWeight;

    @Column(name = "ProductVariantLength")
    private String productVariantLength;

    @Column(name = "ProductVariantPrice")
    private double productVariantPrice;

    @Column(name = "ProductDescription")
    private String productDescription;

    @Column(name = "ProductVariantQuantity")
    private Integer productVariantQuantity;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemImage> images = new LinkedList<>();


    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order ;

}