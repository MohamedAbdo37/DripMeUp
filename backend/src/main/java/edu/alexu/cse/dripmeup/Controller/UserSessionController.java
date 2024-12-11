package edu.alexu.cse.dripmeup.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.alexu.cse.dripmeup.Component.SessionManager;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Excpetion.AuthorizationException;
import edu.alexu.cse.dripmeup.Excpetion.HandlerException;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import edu.alexu.cse.dripmeup.Service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/5/users")

public class UserSessionController {

    private long sessionID = 123456789;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private final SessionManager sessionManager = new SessionManager(userRepository);

    @GetMapping("/login")
    public ResponseEntity<Person> login(@RequestHeader("Email") String email,
            @RequestHeader("Password") String password) {
        Person person = sessionManager.userLogin(email, password);
        if (null == person)
            return ResponseEntity.status(401).body(null);
        else {
            person.setSessionID(sessionID);
            return ResponseEntity.ok(person);
        }
    }

    @GetMapping("/g/login")
    public ResponseEntity<Person> googleLogIn(@RequestHeader("Email") String email) {
        Person person;
        try {
            person = sessionManager.userLogin(email);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
        if (null == person)
            return ResponseEntity.status(401).body(null);
        else {
            person.setSessionID(sessionID);
            return ResponseEntity.ok(person);
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
    public ResponseEntity<String> changePassword(@RequestHeader("NewPassword") String password,
            @RequestHeader("Email") String email) {

        // this part modified by ibrahim
        UserEntity newPassword = new UserEntity();
        newPassword.setPassword(password);
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
    public ResponseEntity<Person> signUp(@RequestBody UserEntity user, @RequestHeader("UserID") long id) {

        if (id != user.getUserID())
            return ResponseEntity.status(400).body(null);
        Person person;
        try {
            person = sessionManager.userSignUp(user);
        } catch (HandlerException e) {
            return ResponseEntity.status(409).body(null);
        }
        return ResponseEntity.ok(person);
    }

    @PostMapping("/g/signup")
    public ResponseEntity<Person> googleSignUp(@RequestBody UserEntity user, @RequestHeader("IDToken") String token) {

        Person person;
        try {
            person = sessionManager.userSignUp(user, token);
        } catch (HandlerException e) {
            return ResponseEntity.status(409).body(null);
        } catch (AuthorizationException e) {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(person);
    }

}
