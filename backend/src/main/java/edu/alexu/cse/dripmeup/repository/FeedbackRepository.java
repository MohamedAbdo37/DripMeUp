package edu.alexu.cse.dripmeup.repository;

import edu.alexu.cse.dripmeup.entity.Feedback;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> { 

    List<Feedback> findByProductId(Long productId);
    List<Feedback> findByUserId(Long userId);
    void deleteById(Long feedback_id);
}
