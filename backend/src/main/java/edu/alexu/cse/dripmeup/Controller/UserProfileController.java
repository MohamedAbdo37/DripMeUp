package edu.alexu.cse.dripmeup.Controller;

import edu.alexu.cse.dripmeup.Entity.EntityIF;
import edu.alexu.cse.dripmeup.Entity.Profile;
import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import edu.alexu.cse.dripmeup.Service.ResponseBodyMessage;
import edu.alexu.cse.dripmeup.Service.UserProfileService;
import edu.alexu.cse.dripmeup.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("users")

public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    private final Long USER_ID = 1L;
    @GetMapping("/")
    public ResponseEntity<?> getUserInfo(){
        try{
            Profile profile = userProfileService.getUserProfile(USER_ID);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("An error occurred while fetching user info"));
        }
    }

}
