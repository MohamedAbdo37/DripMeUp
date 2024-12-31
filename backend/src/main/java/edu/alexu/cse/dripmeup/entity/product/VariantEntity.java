package edu.alexu.cse.dripmeup.entity.product;

import java.util.List;

import edu.alexu.cse.dripmeup.entity.EntityIF;
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


public class VariantEntity implements EntityIF{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "variantID")
    private Long variantID;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "productID")
    private ProductEntity product ;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
    private List<VariantImageEntity> variantImages ;

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

    @Column(name = "Discount")
    private double discount;
    

    public void addVariantImage(VariantImageEntity variantImageEntity) {
        variantImageEntity.setVariant(this);
        this.variantImages.add(variantImageEntity);
    }
}
