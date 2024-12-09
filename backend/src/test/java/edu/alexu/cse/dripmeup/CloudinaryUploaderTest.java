package edu.alexu.cse.dripmeup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CloudinaryUploaderTest {
    @Test
    public void testUploadEmptyImage() {
        CloudinaryUploader uploader = new CloudinaryUploader();
        assertThrows(EmptyImageException.class, () -> {
            uploader.uploadImage(new byte[0]);
        });
    }
    @Test
    public void testCloudinaryObjectCreation() {
        CloudinaryUploader uploader = new CloudinaryUploader();
        assertNotNull(uploader.getConfig());
    }
}
