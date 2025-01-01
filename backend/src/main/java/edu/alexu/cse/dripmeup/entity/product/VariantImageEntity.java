package edu.alexu.cse.dripmeup.entity.product;

import edu.alexu.cse.dripmeup.entity.EntityIF;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "VARIANT_IMAGE")
@Data
public class VariantImageEntity implements EntityIF {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ImageID")
    private Long imageID;


    @Column(name = "Image")

    private String imagePath;

}