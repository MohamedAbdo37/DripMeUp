package edu.alexu.cse.dripmeup.Controller;

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
    private final Long USER_ID = 1L;
//    @GetMapping
//    public ResponseEntity<?> getUserInfo(){
//        return new ResponseEntity<>();
//    }
}
