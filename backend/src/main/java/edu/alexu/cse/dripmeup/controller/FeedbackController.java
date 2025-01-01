package edu.alexu.cse.dripmeup.controller;

import edu.alexu.cse.dripmeup.dto.FeedbackDTO;
import edu.alexu.cse.dripmeup.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/product/{productId}")
    public List<FeedbackDTO> getFeedbackByProductId(@PathVariable Long productId) {
        return feedbackService.getFeedbackByProductId(productId);
    }

    @GetMapping("/user/{userId}")
    public List<FeedbackDTO> getFeedbackByUserId(@PathVariable Long userId) {
        return feedbackService.getFeedbackByUserId(userId);
    }

    @PostMapping
    public FeedbackDTO saveFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.saveFeedback(feedbackDTO);
    }

    @PutMapping("/{feedbackId}")
    public FeedbackDTO updateFeedback(@PathVariable Long feedbackId, @RequestBody FeedbackDTO updatedFeedbackDTO) {
        return feedbackService.updateFeedback(feedbackId, updatedFeedbackDTO);
    }

    @DeleteMapping("/{feedbackId}")
    public void deleteFeedback(@PathVariable Long feedbackId) {
        feedbackService.deleteFeedbackById(feedbackId);
    }
}
