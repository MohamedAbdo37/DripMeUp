package edu.alexu.cse.dripmeup;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
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
    public String uploadImage(byte[] image) throws IOException {
        if (image.length == 0) {
            throw new EmptyImageException("Can't Upload Empty image");
        }
        else if (!isValidImage(image)) {
            throw new IOException("Invalid Image");
        }
        Map resource = cloudinary.uploader().upload(image, ObjectUtils.emptyMap());
        return (String) resource.get("url");
    }

}
