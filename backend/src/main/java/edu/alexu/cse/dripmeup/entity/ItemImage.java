package edu.alexu.cse.dripmeup.entity;

import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ITEM_IMAGE")
@Data

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ImageID")

    private Long imageID;

    @ManyToOne
    @JoinColumn(name = "itemID")
    private OrderItem item;

    @Column(name = "Image")

    private String imagePath;

    }
