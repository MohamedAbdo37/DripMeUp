package edu.alexu.cse.dripmeup.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.component.SessionManager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@CrossOrigin(origins = "http://localhost:8080/api/6/")
@RequestMapping("admin")
public class AdminSessionController {

    @Autowired
    private AdminRepository adminRepository;

    private final SessionManager sessionManager = new SessionManager(adminRepository);

    @GetMapping("signUp")
    public ResponseEntity<Person> adminSignUp(@RequestHeader("UserName") String userName, @RequestHeader("Password") String password) {
        Person person = sessionManager.adminSignUP(userName, password); // null - Person
        if (null != person) 
            return ResponseEntity.ok(person);
        else 
            return ResponseEntity.status(400).body(null);
    }
    
    
}
