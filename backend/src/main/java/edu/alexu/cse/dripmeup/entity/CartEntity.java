package edu.alexu.cse.dripmeup.entity;

import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "CART")
@Data

@Setter
@Getter
@NoArgsConstructor
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "cartID")
    private Long cartID;


    // many cart records reference single user
    @ManyToOne
    private UserEntity user ;

    // many cart records reference single variant
    @ManyToOne
    private VariantEntity variant ;

    @Column(name = "amount")
    private int amount ;

    @Column(name = "time")
    private LocalDateTime time ;

    public CartEntity(UserEntity user, VariantEntity variant, int amount) {
        this.user = user;
        this.variant = variant;
        this.amount = amount;
    }


    // before saved for the first time
    @PrePersist
    protected void onCreate (){
        this.time = LocalDateTime.now() ;
    }

    public void onUpdate (){
        this.time = LocalDateTime.now() ;
    }

}
