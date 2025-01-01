package edu.alexu.cse.dripmeup.entity.product;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import edu.alexu.cse.dripmeup.entity.CategoryEntity;
import edu.alexu.cse.dripmeup.enumeration.ProductState;
import io.jsonwebtoken.lang.Objects;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

    // @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    // private List<VariantEntity> variants;   

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

    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     if (o == null || getClass() != o.getClass()) return false;
    //     ProductEntity that = (ProductEntity) o;
    //     return Objects.equals(productID, that.productID);
    // }

    // @Override
    // public int hashCode() {
    //     return Objects.hash(productID);
    // }

}
