package edu.alexu.cse.dripmeup.entity;

import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "CART")
@Data

public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "cartID")
    private Long cartID;


    // many cart records reference single user
    @ManyToOne
    private UserEntity user ;

    @ManyToOne
    private VariantEntity variant ;

    @Column(name = "amount")
    private int amount ;

    @Column(name = "time")
    private LocalDateTime time ;

    public CartEntity() {
    }


    public CartEntity(UserEntity user, VariantEntity variant, int amount) {
        this.user = user;
        this.variant = variant;
        this.amount = amount;
    }


    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public VariantEntity getVariant() {
        return this.variant;
    }

    public void setVariant(VariantEntity variant) {
        this.variant = variant;
    }

    @PrePersist
    protected void onCreate (){
        this.time = LocalDateTime.now() ;
    }

    public void onUpdate (){
        this.time = LocalDateTime.now() ;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
