package edu.alexu.cse.dripmeup.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
import edu.alexu.cse.dripmeup.Excpetion.InvalidResendCodeException;

import org.springframework.web.bind.annotation.RequestParam;

import edu.alexu.cse.dripmeup.Excpetion.FailedToSendMailException;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/5/users")

public class UserSessionController {

    private final Long sessionID = (long) 123456789;

    @Autowired
    private SessionManager sessionManager;

    @GetMapping("/login")
    public ResponseEntity<Person> login(@RequestHeader("Email") String email,
            @RequestHeader("Password") String password) {
        Person person = sessionManager.userLogin(email, password);
        if (person == null)
            return ResponseEntity.status(401).body(null);
        else {
            person.setSessionID(sessionID);
            try{
                this.sessionManager.sendGreeting(person.getEmail(), person.getUsername());
            } catch (FailedToSendMailException | IOException e) {
                System.out.println(e.getMessage());
            }
            return ResponseEntity.ok(person);
        }
    }

    @GetMapping("/g/login")
    public ResponseEntity<Person> googleLogIn(@RequestHeader("IDToken") String token) {
        Person person;
        try {
            person = sessionManager.userLogin(token);
        } catch (AuthorizationException e) {
            return ResponseEntity.status(500).body(null);
        }
        if (person == null)
            return ResponseEntity.status(401).body(null);
        else {
            person.setSessionID(sessionID);
            try{
                this.sessionManager.sendGreeting(person.getEmail(), person.getUsername());
            } catch (FailedToSendMailException | IOException e) {
                System.out.println(e.getMessage());
            }
            return ResponseEntity.ok(person);
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
            @RequestHeader("Email") String email, @RequestHeader("SessionID") String sessionID) {

        if (!this.sessionID.equals(Long.valueOf(sessionID)))
            return ResponseEntity.status(400).body("Not Authorized");

        try {
            if (this.sessionManager.changePassword(email, password))
                return ResponseEntity.status(200).body(null);
            else
                return ResponseEntity.status(409).body("Failed to update password");
        } catch (AuthorizationException e) {
            return ResponseEntity.status(400).body("Not Authorized");
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
    public ResponseEntity<Person> signUp(@RequestBody UserEntity user, @RequestHeader("UserID") long id) {
        if (id != user.getUserID())
            return ResponseEntity.status(400).body(null);
        Person person;
        try {
            person = sessionManager.userSignUp(user);
        } catch (HandlerException e) {
            return ResponseEntity.status(409).body(null);
        }

        try {
            this.sessionManager.sendGreeting(person.getEmail(), person.getUsername());
        } catch (FailedToSendMailException | IOException e) {
            System.out.println(e.getMessage());
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

        try{
            this.sessionManager.sendGreeting(person.getEmail(), person.getUsername());
        } catch (FailedToSendMailException | IOException e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.ok(person);
    }
}
