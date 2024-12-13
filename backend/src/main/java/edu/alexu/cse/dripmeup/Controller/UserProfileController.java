package edu.alexu.cse.dripmeup.Controller;

import edu.alexu.cse.dripmeup.Entity.EntityIF;
import edu.alexu.cse.dripmeup.Entity.Profile;
import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Excpetion.BadInputException;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import edu.alexu.cse.dripmeup.Service.ResponseBodyMessage;
import edu.alexu.cse.dripmeup.Service.UserProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("users")

public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    private final Long USER_ID = 1L;

    @GetMapping("/")
    public ResponseEntity<?> getUserInfo() {
        try {
            Profile profile = userProfileService.getUserProfile(USER_ID);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ResponseBodyMessage.error("An error occurred while fetching user info"));
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> changeUserInfo(@RequestBody HashMap<String, String> body) {
        try {
            userProfileService.updateUserInfo(USER_ID, body);
        } catch (BadInputException e) {
            return ResponseEntity.status(400).body(ResponseBodyMessage.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ResponseBodyMessage.error("An error occurred while updating user info"));
        }
        return ResponseEntity.ok(ResponseBodyMessage.message("User info updated successfully"));
    }

    @PutMapping("/photo")
    public ResponseEntity<?> changeUserPhoto(@RequestBody byte[] body) {
        try {
            userProfileService.updateUserPhoto(USER_ID, body);
        } catch (BadInputException e) {
            return ResponseEntity.status(400).body(ResponseBodyMessage.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ResponseBodyMessage.error("An error occurred while uploading your photo"));
        }
        return ResponseEntity.ok(ResponseBodyMessage.message("Photo uploaded successfully"));
    }

    @DeleteMapping("/photo")
    public ResponseEntity<?> deleteUserPhoto() {
        try {
            userProfileService.deleteUserPhoto(USER_ID);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ResponseBodyMessage.error("An error occurred while removing your photo"));
        }
        return ResponseEntity.ok(ResponseBodyMessage.message("Photo removed successfully"));
    }

}
