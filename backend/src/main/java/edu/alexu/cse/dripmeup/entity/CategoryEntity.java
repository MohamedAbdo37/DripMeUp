package edu.alexu.cse.dripmeup.entity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CATEGORY")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryID")
    private Long id;

    @Column(name = "CategoryName")
    private String name;

    @Column(name = "CategoryDescription")
    private String description;

    @Column(name = "DateCreated")
    private LocalDateTime dateCreated ;

    @Column(name = "ParentCategoryID")
    private Long parentID;

    @OneToMany(mappedBy = "parentID", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CategoryEntity> subcategories;

    @ManyToMany(mappedBy = "categories")
    private Set<ProductEntity> products;

    @PrePersist
    protected void onCreate (){
        this.dateCreated = LocalDateTime.now() ;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTime() {
        return this.dateCreated;
    }

    public Long getParentID() {
        return this.parentID;
    }

    public List<CategoryEntity> getSubcategories() {
        return subcategories;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addChild(CategoryEntity child) {
        if (this.subcategories == null) {
            this.subcategories = List.of(child);
            return;
        }
        this.subcategories.add(child);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentId(Optional<Long> parentID) {
        this.parentID = parentID.orElse(null);
    }

    public void addProduct(ProductEntity productEntity) {
        if(this.products != null)
            this.products.add(productEntity);
    }
}
