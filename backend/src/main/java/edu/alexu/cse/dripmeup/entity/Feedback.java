package edu.alexu.cse.dripmeup.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "ProductID", nullable = false)
    private Long productId;

    @Column(name = "UserID", nullable = false)
    private Long userId;

    @Column(name = "time", nullable = false, updatable = false)
    private LocalDateTime time = LocalDateTime.now();

    @Column(name = "feedback", nullable = false)
    private String feedback;

    
}
