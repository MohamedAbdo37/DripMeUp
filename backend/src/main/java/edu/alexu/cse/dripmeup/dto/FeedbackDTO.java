package edu.alexu.cse.dripmeup.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
    private Long feedback_id;       
    private Long ProductID;        
    private Long UserID;           
    private LocalDateTime time; 
    private String feedback;

}