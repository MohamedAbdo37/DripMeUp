package edu.alexu.cse.dripmeup.service;

import edu.alexu.cse.dripmeup.dto.FeedbackDTO;
import edu.alexu.cse.dripmeup.entity.Feedback;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.repository.FeedbackRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    private FeedbackDTO convertToDTO(Feedback feedback) {
        return new FeedbackDTO(
                feedback.getFeedback_id(),
                feedback.getProduct().getProductID(),
                feedback.getUser().getUserID(),
                feedback.getFeedback()
        );
    }

    private Feedback convertToEntity(FeedbackDTO feedbackDTO) {
        Feedback feedback = new Feedback();
        feedback.setFeedback_id(feedbackDTO.getFeedbackId());
        feedback.setProduct(new ProductEntity(feedbackDTO.getProductId(), null, null, null, null));
        feedback.setUser(new UserEntity(feedbackDTO.getUserId(), null, null, null, null, null, null, null, null));
        feedback.setFeedback(feedbackDTO.getFeedback());
        return feedback;
    }

    private List<FeedbackDTO> convertToDTOList(List<Feedback> feedbackList) {
        return feedbackList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FeedbackDTO> getFeedbackByProductId(Long productId) {
        List<Feedback> feedbackList = feedbackRepository.findByProduct_ProductID(productId);
        return convertToDTOList(feedbackList);
    }

    public List<FeedbackDTO> getFeedbackByUserId(Long userId) {
        List<Feedback> feedbackList = feedbackRepository.findByUser_UserID(userId);
        return convertToDTOList(feedbackList);
    }

    public FeedbackDTO saveFeedback(FeedbackDTO feedbackDTO) {
        Feedback feedback = convertToEntity(feedbackDTO);  
        Feedback savedFeedback = feedbackRepository.save(feedback);  
        return convertToDTO(savedFeedback);  
    }

    public void deleteFeedbackById(Long feedback_id) {
        feedbackRepository.deleteById(feedback_id);
    }

    public FeedbackDTO updateFeedback(Long feedbackId, FeedbackDTO updatedFeedbackDTO) {
        Feedback existingFeedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + feedbackId));

        existingFeedback.setFeedback(updatedFeedbackDTO.getFeedback());
        existingFeedback.setProduct(new ProductEntity(updatedFeedbackDTO.getProductId(), null, null, null, null));
        existingFeedback.setUser(new UserEntity(updatedFeedbackDTO.getUserId(), null, null, null, null, null, null, null, null)); 

        Feedback savedFeedback = feedbackRepository.save(existingFeedback);  
        return convertToDTO(savedFeedback);  
    }
}
