package edu.alexu.cse.dripmeup.Controller;

import edu.alexu.cse.dripmeup.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.alexu.cse.dripmeup.Component.SessionManager;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Excpetion.AuthorizationException;
import edu.alexu.cse.dripmeup.Excpetion.HandlerException;
import edu.alexu.cse.dripmeup.Service.JwtService;

@RestController
@CrossOrigin
@RequestMapping("/api/5/users")
public class UserSessionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestHeader("Email") String email,
                                        @RequestHeader("Password") String password) {
        Person person = sessionManager.userLogin(email, password);
        if (person == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        } else {
            String token = jwtService.generateToken(email, "ROLE_USER", userRepository.findByEmail(email).getUserID());
            return ResponseEntity.ok(token);
        }
    }

    @GetMapping("/g/login")
    public ResponseEntity<String> googleLogIn(@RequestHeader("IDToken") String token) {
        Person person;
        try {
            person = sessionManager.userLogin(token);
        } catch (AuthorizationException e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
        if (person == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        } else {
            String jwtToken = jwtService.generateToken(person.getEmail(), "ROLE_USER", userRepository.findByEmail(person.getEmail()).getUserID());
            return ResponseEntity.ok(jwtToken);
        }
    }
    @GetMapping("/getUsername")
    public ResponseEntity<Person> getUsername(@RequestHeader("Email") String email) {
        try {
            Person person = sessionManager.forgetPasswordPerson(email);
            return ResponseEntity.ok(person);
        } catch (AuthorizationException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestHeader("NewPassword") String password,
                                                 @RequestHeader("Email") String email) {

        try{
            if(this.sessionManager.changePassword(email, password))
                return ResponseEntity.status(200).body(null);
            else
                return ResponseEntity.status(400).body("Failed to update password");
        } catch (AuthorizationException e) {
            return ResponseEntity.status(400).body("Not Authorized");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserEntity user, @RequestHeader("UserID") long id) {
        if (id != user.getUserID()) {
            return ResponseEntity.status(400).body("Bad Request");
        }
        Person person;
        try {
            person = sessionManager.userSignUp(user);
        } catch (HandlerException e) {
            return ResponseEntity.status(409).body("Conflict");
        }
        String token = jwtService.generateToken(user.getEmail(), "ROLE_USER",  userRepository.findByEmail(user.getEmail()).getUserID());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/g/signup")
    public ResponseEntity<String> googleSignUp(@RequestBody UserEntity user, @RequestHeader("IDToken") String token) {
        Person person;
        try {
            person = sessionManager.userSignUp(user, token);
        } catch (HandlerException e) {
            return ResponseEntity.status(409).body("Conflict");
        } catch (AuthorizationException e) {
            return ResponseEntity.status(400).body("Bad Request");
        }
        String jwtToken = jwtService.generateToken(user.getEmail(), "ROLE_USER",  userRepository.findByEmail(user.getEmail()).getUserID());
        return ResponseEntity.ok(jwtToken);
    }
}