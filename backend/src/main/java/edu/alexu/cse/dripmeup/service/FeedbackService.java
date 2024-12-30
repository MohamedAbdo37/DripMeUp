package edu.alexu.cse.dripmeup.service;

import edu.alexu.cse.dripmeup.entity.Feedback;
import edu.alexu.cse.dripmeup.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> getFeedbackByProductId(Long productId) {
        return feedbackRepository.findByProductId(productId);
    }

    public List<Feedback> getFeedbackByUserId(Long userId) {
        return feedbackRepository.findByUserId(userId);
    }

    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public void deleteFeedbackById(Long feedback_id) {
        feedbackRepository.deleteById(feedback_id);
    }
}
