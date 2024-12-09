package edu.alexu.cse.dripmeup.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.component.SessionManager;
import edu.alexu.cse.dripmeup.Service.AdminService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@CrossOrigin(origins = "http://localhost:8080/api/6/")
@RequestMapping("admin")
public class AdminSessionController {

    private static final long SUPER_ID = 123456789;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminService adminService = new AdminService();

    private final SessionManager sessionManager = new SessionManager(adminRepository);

    @PostMapping("signUp")
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
    public ResponseEntity<String> login(@RequestHeader("UserName") String userName,
            @RequestHeader("Password") String password) {
        System.out.println("Admin Login request received");
        System.out.println("Username: " + userName);
        System.out.println("Password: " + password);

        boolean isAuthenticated = adminService.adminLogin(userName, password);
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Admin login failed");
        }
    }

}
