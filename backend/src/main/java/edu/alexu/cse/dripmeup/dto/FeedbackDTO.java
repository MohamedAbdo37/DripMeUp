package edu.alexu.cse.dripmeup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
    private Long feedbackId;    
    private Long productId;     
    private Long userId;        
    private String feedback;
}
