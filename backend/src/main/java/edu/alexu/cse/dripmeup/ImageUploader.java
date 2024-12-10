package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.Entity.Image;

import java.io.IOException;

public interface ImageUploader {
    public String uploadImage(byte[] image) throws IOException;

    public void deleteImage(String path) throws IOException;

    public String updateImage(String path, byte[] image) throws IOException; // return a new path

}
