package edu.alexu.cse.dripmeup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
    public void testUpdateToEmptyImage() {
        CloudinaryUploader uploader = new CloudinaryUploader();
        assertThrows(EmptyImageException.class, () -> {
            uploader.updateImage("some_id", new byte[0]);
        });
    }
    @Test
    public void testUploadInvalidImage() {
        CloudinaryUploader uploader = new CloudinaryUploader();
        assertThrows(IOException.class, () -> {
            uploader.uploadImage(new byte[100]);
        });
    }
    @Test
    public void testUpdateToInvalidImage() {
        CloudinaryUploader uploader = new CloudinaryUploader();
        assertThrows(IOException.class, () -> {
            uploader.updateImage("some_id", new byte[100]);
        });
    }
    @Test
    public void testCloudinaryObjectCreation() {
        CloudinaryUploader uploader = new CloudinaryUploader();
        assertNotNull(uploader.getConfig());
    }

//    @Test
//    public void testNormalUpload() throws IOException {
//        CloudinaryUploader uploader = new CloudinaryUploader();
//        File file = new File("C:\\Users\\Lenovo\\Desktop\\DripMeUp\\frontend\\src\\assets\\DripMeUp.png");
//        byte[] fileContent = Files.readAllBytes(file.toPath());
//        System.out.println(uploader.uploadImage(fileContent));
//    }
//
//    @Test
//    public void testDelete() {
//        CloudinaryUploader uploader = new CloudinaryUploader();
//        assertDoesNotThrow(()-> {
//            uploader.deleteImage("aikl7rdcvgdlt79zsf1g");
//        });
//
//    }
//    @Test
//    public void uploadTest() throws IOException {
//        CloudinaryUploader uploader = new CloudinaryUploader();
//        File file = new File("C:\\Users\\Lenovo\\Desktop\\DripMeUp\\frontend\\src\\assets\\logo.png");
//        byte[] fileContent = Files.readAllBytes(file.toPath());
//        System.out.println(uploader.updateImage("dgpnlzuvanrbgdhljwu3", fileContent));
//    }

}
