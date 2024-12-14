package edu.alexu.cse.dripmeup.Excpetion;

public class FailedToSendMailException extends RuntimeException{
    public FailedToSendMailException(String message) {
        super(message);
    }
    
}
