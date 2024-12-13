package edu.alexu.cse.dripmeup;
import org.junit.jupiter.api.Test;


import edu.alexu.cse.dripmeup.Excpetion.EmptyImageException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    public void testCloudIdParsingWithBackslashWithExtension(){
        String path = "dir1\\dir2\\dir3\\cloudId.png";
        assertEquals(CloudinaryUploader.getIdFromPath(path), "cloudId");
    }
    @Test
    public void testCloudIdParsingWithForwardSlashWithExtension(){
        String path = "dir1/dir2/dir3/cloudId.png";
        assertEquals(CloudinaryUploader.getIdFromPath(path), "cloudId");
    }
    @Test
    public void testCloudIdParsingWithBackslashWithoutExtension(){
        String path = "dir1\\dir2\\dir3\\cloudId";
        assertEquals(CloudinaryUploader.getIdFromPath(path), "cloudId");
    }
    @Test
    public void testCloudIdParsingWithForwardSlashWithoutExtension(){
        String path = "dir1/dir2/dir3/cloudId";
        assertEquals(CloudinaryUploader.getIdFromPath(path), "cloudId");
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
//            uploader.deleteImage("https://res.cloudinary.com/dzfkzovzn/image/upload/v1733781773/mld6d517ct2okdywkxi8.png");
//        });
//
//    }
//    @Test
//    public void uploadTest() throws IOException {
//        CloudinaryUploader uploader = new CloudinaryUploader();
//        File file = new File("C:\\Users\\Lenovo\\Desktop\\DripMeUp\\frontend\\src\\assets\\logo.png");
//        byte[] fileContent = Files.readAllBytes(file.toPath());
//        System.out.println(uploader.updateImage("https://res.cloudinary.com/dzfkzovzn/image/upload/v1733782484/zvhcbt9l7itua0iy3744.png", fileContent));
//    }

}
