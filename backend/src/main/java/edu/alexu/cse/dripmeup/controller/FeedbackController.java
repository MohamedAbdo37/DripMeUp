package edu.alexu.cse.dripmeup.controller;

import edu.alexu.cse.dripmeup.entity.Feedback;
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

    @GetMapping("/product/{ProductID}")
    public List<Feedback> getFeedbackByProductId(@PathVariable Long ProductID) {
        return feedbackService.getFeedbackByProductId(ProductID);
    }

    @GetMapping("/user/{UserID}")
    public List<Feedback> getFeedbackByUserId(@PathVariable Long UserID) {
        return feedbackService.getFeedbackByUserId(UserID);
    }

    @PostMapping
    public Feedback addFeedback(@RequestBody Feedback feedback) {
        return feedbackService.saveFeedback(feedback);
    }

    @DeleteMapping("/{feedback_id}")
    public void deleteFeedback(@PathVariable Long feedback_id) {
        feedbackService.deleteFeedbackById(feedback_id);
    }
}
