package edu.alexu.cse.dripmeup.exception;
import java.io.IOException;

public class EmptyImageException extends IOException {
    public EmptyImageException(String message) {
        super(message);
    }
}