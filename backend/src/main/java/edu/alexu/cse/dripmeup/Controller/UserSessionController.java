package edu.alexu.cse.dripmeup.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("users")

public class UserSessionController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/login/{email}_{password}")
    
    public ResponseEntity<String> login(@PathVariable String email, @PathVariable String password) {
        System.out.println("Login request received");
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        boolean isAuthenticated = userService.login(email, password);
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserEntity newUser) {
        System.out.println("Signup request received");
        System.out.println("Email: " + newUser.getEmail());
        System.out.println("Password: " + newUser.getPassword());
        
        String result = userService.signup(newUser);
        if (result.equals("User registered successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(400).body(result);
        }
    }

}
