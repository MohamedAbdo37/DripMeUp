package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.Entity.Image;

import java.io.IOException;

public interface ImageUploader {
    public Image uploadImage(byte[] image) throws IOException;

    public void deleteImage(String path) throws IOException;

    public Image updateImage(String cloudId, byte[] image) throws IOException;

}
