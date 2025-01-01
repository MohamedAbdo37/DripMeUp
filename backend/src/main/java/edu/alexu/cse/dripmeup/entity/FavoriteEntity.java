package edu.alexu.cse.dripmeup.entity;

import edu.alexu.cse.dripmeup.entity.product.VariantEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "FAVORITE")
@Data

@Setter
@Getter
@NoArgsConstructor

public class FavoriteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "favoriteID")
    private Long favoriteID;


    // many cart records reference single user
    @ManyToOne
    private UserEntity user ;

    @ManyToOne
    private VariantEntity variant ;

    @Column(name = "time")
    private LocalDateTime time ;


    public FavoriteEntity(UserEntity user, VariantEntity variant) {
        this.user = user;
        this.variant = variant;
    }

    @PrePersist
    protected void onCreate (){
        this.time = LocalDateTime.now() ;
    }

    public void onUpdate (){
        this.time = LocalDateTime.now() ;
    }

}
