package edu.alexu.cse.dripmeup;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
@Component
public interface ImageUploader {
    public String uploadImage(byte[] image) throws IOException;

    public void deleteImage(String path) throws IOException;

    public String updateImage(String path, byte[] image) throws IOException; // return a new path

    public static boolean isValidImage(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            return false;
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
            return ImageIO.read(bais) != null;
        } catch (IOException e) {
            return false;
        }
    }

}
