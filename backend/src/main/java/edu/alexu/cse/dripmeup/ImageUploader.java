package edu.alexu.cse.dripmeup;

import java.io.IOException;

public interface ImageUploader {
    public String uploadImage(byte[] image) throws IOException;

}
