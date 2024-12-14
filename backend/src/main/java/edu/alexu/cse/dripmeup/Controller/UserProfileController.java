package edu.alexu.cse.dripmeup.Controller;

import edu.alexu.cse.dripmeup.Entity.EntityIF;
import edu.alexu.cse.dripmeup.Entity.Profile;
import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import edu.alexu.cse.dripmeup.Service.ResponseBodyMessage;
import edu.alexu.cse.dripmeup.Service.SecurityService;
import edu.alexu.cse.dripmeup.Service.UserProfileService;

import edu.alexu.cse.dripmeup.excpetion.BadInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/")
    public ResponseEntity<?> getUserInfo(){
        try{
            Long USER_ID = SecurityService.getIdFromSecurityContext();
            System.out.println(USER_ID);
            Profile profile = userProfileService.getUserProfile(USER_ID);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("An error occurred while fetching user info"));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/")
    public ResponseEntity<?> changeUserInfo(@RequestBody HashMap<String, String> body){
        try{
            Long USER_ID = SecurityService.getIdFromSecurityContext();
            userProfileService.updateUserInfo(USER_ID, body);
        }
        catch (BadInputException e){
            return ResponseEntity.status(400).body(ResponseBodyMessage.error(e.getMessage()));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("An error occurred while updating user info"));
        }
        return ResponseEntity.ok(ResponseBodyMessage.message("User info updated successfully"));
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/photo")
    public ResponseEntity<?> changeUserPhoto(@RequestBody byte[] body){
        try{
            Long USER_ID = SecurityService.getIdFromSecurityContext();
            userProfileService.updateUserPhoto(USER_ID, body);
        }
        catch (BadInputException e){
            return ResponseEntity.status(400).body(ResponseBodyMessage.error(e.getMessage()));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("An error occurred while uploading your photo"));
        }
        return ResponseEntity.ok(ResponseBodyMessage.message("Photo uploaded successfully"));
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/photo")
    public ResponseEntity<?> deleteUserPhoto(){
        try{
            Long USER_ID = SecurityService.getIdFromSecurityContext();
            userProfileService.deleteUserPhoto(USER_ID);
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("An error occurred while removing your photo"));
        }
        return ResponseEntity.ok(ResponseBodyMessage.message("Photo removed successfully"));
    }



}
