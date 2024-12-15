package edu.alexu.cse.dripmeup.exception;
public class FailedToSendMailException extends RuntimeException{
    public FailedToSendMailException(String message) {
        super(message);
    }
    
}
