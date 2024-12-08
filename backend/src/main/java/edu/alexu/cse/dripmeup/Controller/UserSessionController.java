package edu.alexu.cse.dripmeup.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import edu.alexu.cse.dripmeup.Service.PersonDirector;
import edu.alexu.cse.dripmeup.Service.UserService;
import edu.alexu.cse.dripmeup.Service.builder.UserPersonBuilder;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("users")

public class UserSessionController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")

    public ResponseEntity<?> login(@RequestHeader("Email") String email, @RequestHeader("Password") String password) {

        boolean isAuthenticated = userService.login(email, password);
        if (isAuthenticated) {
            UserEntity user = userRepository.findByEmail(email);
            Person person = new PersonDirector().construct(new UserPersonBuilder(user, userRepository));
            return ResponseEntity.ok(person);
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @GetMapping("/login")

    public ResponseEntity<?> loginWithGoogle(@RequestHeader("Email") String email) {

        boolean isAuthenticated = userService.logInWithoutPassword(email);
        if (isAuthenticated) {
            UserEntity user = userRepository.findByEmail(email);
            UserEntity response = new UserEntity();
            response.setUserName(user.getUserName());
            response.setGender(user.getGender());
            response.setEmail(user.getEmail());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @GetMapping("getUsername")
    public ResponseEntity<?> getUsername(@RequestHeader("Email") String email) {

        boolean isAuthenticated = userService.logInWithoutPassword(email);
        if (isAuthenticated) {
            UserEntity user = userRepository.findByEmail(email);
            UserEntity response = new UserEntity();
            response.setUserName(user.getUserName());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<String> signUp(@RequestHeader("NewPassword") UserEntity newPassword,
            @RequestHeader("Email") String email) {
        boolean isAuthenticated = userService.logInWithoutPassword(email);
        if (isAuthenticated) {
            if (userService.changePassword(email, newPassword)) {
                return ResponseEntity.ok("Password updated successfully");
            } else {
                return ResponseEntity.status(400).body("Faild to update password");
            }
        } else {
            return ResponseEntity.status(400).body("Not Authorised");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Person> signUp(@RequestBody UserEntity newUser) {
        Person person = null;
        try {
            person = userService.signup(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(person);

    }

}
