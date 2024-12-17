package edu.alexu.cse.dripmeup.entity.product;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "VARIANT_IMAGE")
@Data
public class VariantImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ImageID")
    private Long ImageID;

    @ManyToOne
    @JoinColumn(name = "variantID")
    private VariantEntity variant;

    @Column(name = "Image")
    private String ImagePath;


}
