package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.entity.Feedback;
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

import static org.junit.jupiter.api.Assertions.*;
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
        Feedback feedback1 = new Feedback(1L, 1L, 1L, null, "Great product!");
        Feedback feedback2 = new Feedback(2L, 1L, 2L, null, "Loved it!");

        when(feedbackRepository.findByProductId(1L)).thenReturn(Arrays.asList(feedback1, feedback2));

        List<Feedback> feedbacks = feedbackService.getFeedbackByProductId(1L);

        assertNotNull(feedbacks);
        assertEquals(2, feedbacks.size());
        assertEquals("Great product!", feedbacks.get(0).getFeedback());
        assertEquals("Loved it!", feedbacks.get(1).getFeedback());

        verify(feedbackRepository, times(1)).findByProductId(1L);
    }

    @Test
    void testGetFeedbackByProductIdReturnsEmptyList() {
        when(feedbackRepository.findByProductId(1L)).thenReturn(Collections.emptyList());

        List<Feedback> feedbacks = feedbackService.getFeedbackByProductId(1L);

        assertNotNull(feedbacks);
        assertTrue(feedbacks.isEmpty());

        verify(feedbackRepository, times(1)).findByProductId(1L);
    }

    @Test
    void testGetFeedbackByUserIdReturnsResults() {
        Feedback feedback1 = new Feedback(1L, 1L, 1L, null, "Great product!");
        Feedback feedback2 = new Feedback(2L, 2L, 1L, null, "Loved it!");

        when(feedbackRepository.findByUserId(1L)).thenReturn(Arrays.asList(feedback1, feedback2));

        List<Feedback> feedbacks = feedbackService.getFeedbackByUserId(1L);

        assertNotNull(feedbacks);
        assertEquals(2, feedbacks.size());
        assertEquals("Great product!", feedbacks.get(0).getFeedback());
        assertEquals("Loved it!", feedbacks.get(1).getFeedback());

        verify(feedbackRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetFeedbackByUserIdReturnsEmptyList() {
        when(feedbackRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        List<Feedback> feedbacks = feedbackService.getFeedbackByUserId(1L);

        assertNotNull(feedbacks);
        assertTrue(feedbacks.isEmpty());

        verify(feedbackRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testSaveFeedback() {
        Feedback feedback = new Feedback(null, 1L, 1L, null, "Amazing product!");
        Feedback savedFeedback = new Feedback(1L, 1L, 1L, null, "Amazing product!");

        when(feedbackRepository.save(feedback)).thenReturn(savedFeedback);

        Feedback result = feedbackService.saveFeedback(feedback);

        assertNotNull(result);
        assertEquals(1L, result.getFeedback_id());
        assertEquals("Amazing product!", result.getFeedback());

        verify(feedbackRepository, times(1)).save(feedback);
    }

    @Test
    void testSaveFeedbackWithNullFields() {
        Feedback feedback = new Feedback(null, null, null, null, null);

        when(feedbackRepository.save(feedback)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> feedbackService.saveFeedback(feedback));

        verify(feedbackRepository, times(1)).save(feedback);
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
}
