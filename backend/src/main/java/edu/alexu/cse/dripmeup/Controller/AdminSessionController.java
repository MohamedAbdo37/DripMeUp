package edu.alexu.cse.dripmeup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Component.SessionManager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/6/admin")
public class AdminSessionController {

    private static final long SUPER_ID = 123456789;
    private long sessionID = 123456789;

    @Autowired
    private AdminRepository adminRepository;

    private final SessionManager sessionManager = new SessionManager(adminRepository);

    @PostMapping("signup")
    public ResponseEntity<Person> adminSignUp(@RequestHeader("UserName") String userName,
            @RequestHeader("Password") String password, @RequestHeader("SuperID") long superID) {

        if (superID == SUPER_ID)
            return ResponseEntity.status(400).body(null);
        Person person = sessionManager.adminSignUP(userName, password);
        if (null != person)
            return ResponseEntity.ok(person);
        else
            return ResponseEntity.status(409).body(null);
    }

    @GetMapping("/login")
    public ResponseEntity<Person> login(@RequestHeader("UserName") String userName,
            @RequestHeader("Password") String password) {

        System.out.println("Admin Login request received");
        System.out.println("Username: " + userName);
        System.out.println("Password: " + password);

        Person person = sessionManager.adminLogin(userName, password);
        if (null == person)
            return ResponseEntity.status(401).body(null);
        else {
            person.setSessionID(sessionID);
            return ResponseEntity.ok(person);
        }

    }

}
