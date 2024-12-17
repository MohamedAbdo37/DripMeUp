package edu.alexu.cse.dripmeup.entity.product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUCT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "productID")
    private Long productID;


    @Column(name = "Description")
    private String description ;

    @Column(name = "DateOfCreation")
    private LocalDateTime time ;

    @OneToMany(mappedBy= "Product", cascade = CascadeType.ALL)
    private List<VariantEntity> variants ;

    @OneToMany(mappedBy= "Product")
    private List<ItemEntity> items ;

    @Column(name = "Price")
    private double price ;

    @Column(name = "Stock")
    private int stock ;

    @Column(name = "Sold")
    private int sold ;

    @Column(name = "State")
    private ProductState state ;

    @ManyToMany
    @JoinTable(
            name = "PRODUCT_CATEGORY",
            joinColumns = @JoinColumn(name = "productID"),
            inverseJoinColumns = @JoinColumn(name = "categoryID")
    )
    private Set<CategoryEntity> categories;

    @PrePersist
    protected void onCreate (){
        this.time = LocalDateTime.now() ;
    }

}
