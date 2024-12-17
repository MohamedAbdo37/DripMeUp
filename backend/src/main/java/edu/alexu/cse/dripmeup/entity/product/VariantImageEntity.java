package edu.alexu.cse.dripmeup.entity.product;

import edu.alexu.cse.dripmeup.entity.EntityIF;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "VARIANT_IMAGE")
@Data
public class VariantImageEntity implements EntityIF {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ImageID")
    private Long imageID;

    @ManyToOne
    @JoinColumn(name = "variantID")
    private VariantEntity variant;

    @Column(name = "Image")
    private String imagePath;

}