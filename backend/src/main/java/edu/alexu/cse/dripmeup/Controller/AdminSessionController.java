package edu.alexu.cse.dripmeup.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import edu.alexu.cse.dripmeup.Component.SessionManager;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Service.JwtService;

@RestController
@CrossOrigin
@RequestMapping("/api/6/admin")
public class AdminSessionController {


    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private Environment env;
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/signup")
    public ResponseEntity<String> adminSignUp(@RequestHeader("UserName") String userName,
                                              @RequestHeader("Password") String password) {
        Person person = sessionManager.adminSignUP(userName, password);
        if (person != null) {
            return ResponseEntity.ok("Admin created successfully");
        } else {
            return ResponseEntity.status(409).body("Conflict");
        }
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestHeader("UserName") String userName,
                                        @RequestHeader("Password") String password) {
        String superAdminPassword = env.getProperty("super.admin.password");
        String superAdminName = env.getProperty("super.admin.name");


        if (superAdminName.equals(userName) && superAdminPassword.equals(password)) {
            String token = jwtService.generateToken(userName, "ROLE_SUPER_ADMIN", 0L);
            return ResponseEntity.ok(token);
        }

        Person person = sessionManager.adminLogin(userName, password);
        if (person == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        } else {
            String token = jwtService.generateToken(userName, "ROLE_ADMIN", 0L);
            return ResponseEntity.ok(token);
        }
    }
}