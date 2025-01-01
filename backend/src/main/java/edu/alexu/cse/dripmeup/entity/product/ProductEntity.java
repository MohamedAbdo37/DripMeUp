package edu.alexu.cse.dripmeup.entity.product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import edu.alexu.cse.dripmeup.entity.EntityIF;
import edu.alexu.cse.dripmeup.enumeration.ProductState;

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
    private String description;

    @Column(name = "DateOfCreation")
    private LocalDateTime time;

    @OneToMany(mappedBy = "variantID", cascade = CascadeType.ALL)
    private List<VariantEntity> variants;   

    @Column(name = "State")
    private ProductState state;


    @ManyToMany
    @JoinTable(
        name = "PRODUCT_CATEGORY",
        joinColumns = @JoinColumn(name = "productID"),
        inverseJoinColumns = @JoinColumn(name = "CategoryID")
    )
    private Set<CategoryEntity> categories;

    @PrePersist
    protected void onCreate() {
        this.time = LocalDateTime.now();
    }

    public void addCategory(CategoryEntity c) {
        if(this.categories != null){
            this.categories.add(c);
        }
        else{
            this.categories = new HashSet<>();
            this.categories.add(c);
        }

    }

    public void addVarinat(VariantEntity v)  {
        if(this.variants != null){
            this.variants.add(v);
        } else {
            this.variants = new ArrayList<>();
            this.variants.add(v);
        }

    }


}
