package edu.alexu.cse.dripmeup.Controller;

import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("users/profile")
public class UserProfileController {
    private final UserProfileService userProfileService;
    @Autowired
    public UserProfileController(UserProfileService userProfileService){
        this.userProfileService = userProfileService;
    }
    @GetMapping("/{userId}")
    public ResponseEntity<Person> getUserProfile(@PathVariable String userId) {
        Person person = userProfileService.getUserProfile(userId);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }
    @GetMapping("/photo/{userId}")
    public ResponseEntity<byte[]> getUserProfilePhoto(@PathVariable String userId){
        byte[] photo = userProfileService.getUserProfilePhoto(userId);
        if (photo != null) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpeg");
        return new ResponseEntity<>(photo, headers, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
    @PostMapping("/photo/{userId}")
    public ResponseEntity<String> changeUserProfilePhoto(@PathVariable String userId, @RequestBody byte[] photo) {
        userProfileService.changeUserProfilePhoto(userId, photo);
        return new ResponseEntity<>("photo changed successfully",HttpStatus.OK);
    }

    @PostMapping("/username/{userId}")
    public ResponseEntity<String> changeUsername(@PathVariable String userId, @RequestBody String username) {
        userProfileService.changeUsername(userId, username);
        return new ResponseEntity<>("username changed successfully",HttpStatus.OK);
    }
}
