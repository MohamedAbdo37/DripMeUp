package edu.alexu.cse.dripmeup.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "ITEM")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "itemId")
    private Long id;
    @Column(name = "ProductName")
    private String ProductName;
    @Column(name = "ProductVariantSize")
    private String ProductVariantSize;
    @Column(name = "ProductVariantColor")
    private String ProductVariantColor;
    @Column(name = "ProductVariantPrice")
    private float ProductVariantPrice;
    @Column(name = "ProductVariantQuantity")
    private Integer ProductVariantQuantity;

}
