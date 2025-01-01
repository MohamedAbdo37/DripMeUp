package edu.alexu.cse.dripmeup.controller;


import java.io.IOException;
import edu.alexu.cse.dripmeup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.alexu.cse.dripmeup.component.SessionManager;
import edu.alexu.cse.dripmeup.entity.Person;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.exception.AuthorizationException;
import edu.alexu.cse.dripmeup.exception.HandlerException;

import edu.alexu.cse.dripmeup.exception.InvalidResendCodeException;

import edu.alexu.cse.dripmeup.exception.FailedToSendMailException;

import edu.alexu.cse.dripmeup.service.JwtService;


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

    private final Long sessionID = (long) 123456789;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestHeader("Email") String email,
                                        @RequestHeader("Password") String password) {
        Person person = sessionManager.userLogin(email, password);

        if (person == null)
            return ResponseEntity.status(401).body(null);
        else {
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

        if (person == null)
            return ResponseEntity.status(401).body(null);
        else {
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


    @GetMapping("signup/code")
    public ResponseEntity<String> sendCodeSignUp(@RequestHeader("Email") String email,
                                                 @RequestHeader("UserName") String userName) {
        try {
            String code = sessionManager.generateCodeSignUp(email, userName);
            return ResponseEntity.ok(code);
        } catch (IOException | FailedToSendMailException ex) {
            return ResponseEntity.status(409).body("Email not sent");
        } catch (InvalidResendCodeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/forgotPassword/code")
    public ResponseEntity<String> sendCodeForgetPassword(@RequestHeader("Email") String email,
                                                         @RequestHeader("UserName") String userName) {
        System.out.println("userName" + userName);
        try {
            String code = sessionManager.generateCodeForgetPassword(email, userName);
            return ResponseEntity.ok(code);
        } catch (IOException | FailedToSendMailException ex) {
            return ResponseEntity.status(409).body("Email not sent");
        }catch (InvalidResendCodeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/checkCode")
    public ResponseEntity<Long> checkCodeForgetPassword(@RequestHeader("CodeID") String codeID,
                                                        @RequestHeader("Code") String code) {
        if (this.sessionManager.checkCode(codeID, code))
            return ResponseEntity.ok(this.sessionID);
        else
            return ResponseEntity.status(400).body(null);
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

        try {
            this.sessionManager.sendGreeting(person.getEmail(), person.getUsername());
        } catch (FailedToSendMailException | IOException e) {
            System.out.println(e.getMessage());
        }

        String token = jwtService.generateToken(person.getEmail(), "ROLE_USER",  userRepository.findByEmail(person.getEmail()).getUserID());
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

        try{
            this.sessionManager.sendGreeting(person.getEmail(), person.getUsername());
        } catch (FailedToSendMailException | IOException e) {
            System.out.println(e.getMessage());
        }

        String jwtToken = jwtService.generateToken(person.getEmail(), "ROLE_USER",  userRepository.findByEmail(person.getEmail()).getUserID());
        return ResponseEntity.ok(jwtToken);

    }
}