package edu.alexu.cse.dripmeup.Excpetion;

public class SendMailException extends RuntimeException{
    public SendMailException(String message) {
        super(message);
    }
    
}
