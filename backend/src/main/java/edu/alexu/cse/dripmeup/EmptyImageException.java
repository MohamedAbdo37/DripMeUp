package edu.alexu.cse.dripmeup;

import java.io.IOException;

public class EmptyImageException extends IOException {
    public EmptyImageException(String message) {
        super(message);
    }
}