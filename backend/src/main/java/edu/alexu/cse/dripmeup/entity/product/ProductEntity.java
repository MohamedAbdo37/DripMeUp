package edu.alexu.cse.dripmeup.entity.product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import jakarta.persistence.*;

import edu.alexu.cse.dripmeup.entity.EntityIF;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUCT")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductEntity implements EntityIF {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "productID")
    private Long productID;

    @Column(name = "Description")
    private String description ;

    @Column(name = "DateOfCreation")
    private LocalDateTime time ;

    @OneToMany(mappedBy= "Product")
    private List<ItemEntity> items ;

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