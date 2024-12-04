package edu.alexu.cse.dripmeup.Controller;

import edu.alexu.cse.dripmeup.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("admin")
public class AdminSessionController {
    @Autowired
    private AdminService adminService = new AdminService();

    @GetMapping("/login/{userName}_{password}")
    public ResponseEntity<String> login(@PathVariable String userName, @PathVariable String password) {
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
