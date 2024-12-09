package edu.alexu.cse.dripmeup;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import edu.alexu.cse.dripmeup.Entity.Image;
import io.github.cdimascio.dotenv.Dotenv;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
public class CloudinaryUploader implements ImageUploader{

    private Dotenv dotenv;
    private Cloudinary cloudinary;



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

    public String getConfig() {
        return cloudinary.config.cloudName;
    }


    public CloudinaryUploader() {
        dotenv = Dotenv.load();
        cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
    }

    @Override
    public Image uploadImage(byte[] image) throws IOException {
        if (image.length == 0) {
            throw new EmptyImageException("Can't Upload Empty image");
        }
        else if (!isValidImage(image)) {
            throw new IOException("Invalid Image");
        }
        Map resource = cloudinary.uploader().upload(image, ObjectUtils.emptyMap());
        return new Image((String)resource.get("url"), (String)resource.get("public_id"));
    }

    @Override
    public void deleteImage(String cloudId) throws IOException {
        cloudinary.uploader().destroy(cloudId, ObjectUtils.emptyMap());
    }

    @Override
    public Image updateImage(String cloudId, byte[] image) throws IOException {
        if (image.length == 0) {
            throw new EmptyImageException("Can't Upload Empty image");
        }
        else if (!isValidImage(image)) {
            throw new IOException("Invalid Image");
        }
        cloudinary.uploader().destroy(cloudId, ObjectUtils.emptyMap());
        Map resource = cloudinary.uploader().upload(image, ObjectUtils.emptyMap());
        return new Image((String)resource.get("url"), (String)resource.get("public_id"));
    }

}
