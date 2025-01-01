package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.dto.FeedbackDTO;
import edu.alexu.cse.dripmeup.entity.Feedback;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.entity.product.ProductEntity;
import edu.alexu.cse.dripmeup.repository.FeedbackRepository;
import edu.alexu.cse.dripmeup.service.FeedbackService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFeedbackByProductIdReturnsResults() {
        Feedback feedback1 = new Feedback();
        feedback1.setFeedback_id(1L);
        feedback1.setProduct(new ProductEntity(1L, null, null, null, null));
        feedback1.setUser(new UserEntity(1L, null, null, null, null, null, null, null, null));
        feedback1.setFeedback("Great product!");

        Feedback feedback2 = new Feedback();
        feedback2.setFeedback_id(2L);
        feedback2.setProduct(new ProductEntity(1L, null, null, null, null));
        feedback2.setUser(new UserEntity(2L, null, null, null, null, null, null, null, null));
        feedback2.setFeedback("Loved it!");

        when(feedbackRepository.findByProduct_ProductID(1L)).thenReturn(Arrays.asList(feedback1, feedback2));

        List<FeedbackDTO> feedbacks = feedbackService.getFeedbackByProductId(1L);

        assertNotNull(feedbacks);
        assertEquals(2, feedbacks.size());
        assertEquals("Great product!", feedbacks.get(0).getFeedback());
        assertEquals("Loved it!", feedbacks.get(1).getFeedback());

        verify(feedbackRepository, times(1)).findByProduct_ProductID(1L);
    }


    @Test
    void testGetFeedbackByUserIdReturnsEmptyList() {
        when(feedbackRepository.findByUser_UserID(1L)).thenReturn(Collections.emptyList());

        List<FeedbackDTO> feedbacks = feedbackService.getFeedbackByUserId(1L);

        assertNotNull(feedbacks);
        assertTrue(feedbacks.isEmpty());

        verify(feedbackRepository, times(1)).findByUser_UserID(1L);
    }


    @Test
    void testSaveFeedback() {
        FeedbackDTO feedbackDTO = new FeedbackDTO(null, 1L, 1L, "Amazing product!");
        Feedback feedback = new Feedback();
        feedback.setFeedback_id(null);
        feedback.setProduct(new ProductEntity(1L, null, null, null, null));
        feedback.setUser(new UserEntity(1L, null, null, null, null, null, null, null, null));
        feedback.setFeedback("Amazing product!");

        Feedback savedFeedback = new Feedback();
        savedFeedback.setFeedback_id(1L);
        savedFeedback.setProduct(new ProductEntity(1L, null, null, null, null));
        savedFeedback.setUser(new UserEntity(1L, null, null, null, null, null, null, null, null));
        savedFeedback.setFeedback("Amazing product!");

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(savedFeedback);

        FeedbackDTO result = feedbackService.saveFeedback(feedbackDTO);

        assertNotNull(result);
        assertEquals(1L, result.getFeedbackId());
        assertEquals("Amazing product!", result.getFeedback());

        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }


    @Test
    void testSaveFeedbackWithNullFields() {
        FeedbackDTO feedbackDTO = new FeedbackDTO(null, null, null, null);
        Feedback feedback = new Feedback();
        feedback.setFeedback_id(null);
        feedback.setProduct(null);
        feedback.setUser(null);
        feedback.setFeedback(null);

        when(feedbackRepository.save(any(Feedback.class))).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> feedbackService.saveFeedback(feedbackDTO));

        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void testDeleteFeedbackByIdExists() {
        Long feedbackId = 1L;

        doNothing().when(feedbackRepository).deleteById(feedbackId);

        assertDoesNotThrow(() -> feedbackService.deleteFeedbackById(feedbackId));

        verify(feedbackRepository, times(1)).deleteById(feedbackId);
    }


    @Test
    void testDeleteFeedbackByIdNotExists() {
        Long feedbackId = 1L;

        doThrow(new IllegalArgumentException("Feedback not found"))
                .when(feedbackRepository)
                .deleteById(feedbackId);

        assertThrows(IllegalArgumentException.class, () -> feedbackService.deleteFeedbackById(feedbackId));

        verify(feedbackRepository, times(1)).deleteById(feedbackId);
    }

    @Test
    void testUpdateFeedback() {
        Long feedbackId = 1L;
        FeedbackDTO updatedFeedbackDTO = new FeedbackDTO(feedbackId, 2L, 2L, "Updated feedback");

        Feedback existingFeedback = new Feedback();
        existingFeedback.setFeedback_id(feedbackId);
        existingFeedback.setProduct(new ProductEntity(1L, null, null, null, null));
        existingFeedback.setUser(new UserEntity(1L, null, null, null, null, null, null, null, null));
        existingFeedback.setFeedback("Old feedback");

        Feedback updatedFeedback = new Feedback();
        updatedFeedback.setFeedback_id(feedbackId);
        updatedFeedback.setProduct(new ProductEntity(2L, null, null, null, null));
        updatedFeedback.setUser(new UserEntity(2L, null, null, null, null, null, null, null, null));
        updatedFeedback.setFeedback("Updated feedback");

        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(existingFeedback));
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(updatedFeedback);

        FeedbackDTO result = feedbackService.updateFeedback(feedbackId, updatedFeedbackDTO);

        assertNotNull(result);
        assertEquals(feedbackId, result.getFeedbackId());
        assertEquals("Updated feedback", result.getFeedback());

        verify(feedbackRepository, times(1)).findById(feedbackId);
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }
}
