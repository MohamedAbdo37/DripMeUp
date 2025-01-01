package edu.alexu.cse.dripmeup.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import edu.alexu.cse.dripmeup.entity.product.ProductEntity;

@Entity
@Table(name = "Feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "feedback_id")
    private Long feedback_id;

    @ManyToOne
    @JoinColumn(name = "productID", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private UserEntity user;

    @Column(name = "time", nullable = false, updatable = false)
    private LocalDateTime time = LocalDateTime.now();

    @Column(name = "feedback", nullable = false)
    private String feedback;

    
}
