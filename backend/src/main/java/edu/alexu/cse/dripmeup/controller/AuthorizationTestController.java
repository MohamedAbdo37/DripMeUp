package edu.alexu.cse.dripmeup.controller;

import edu.alexu.cse.dripmeup.service.ResponseBodyMessage;
import edu.alexu.cse.dripmeup.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("ping")
public class AuthorizationTestController {
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public ResponseEntity<?> pingUser(){
        try{
            Long USER_ID = SecurityService.getIdFromSecurityContext();
            return ResponseEntity.ok("You have user access");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("error"));
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> pingAdmin(){
        try{
            return ResponseEntity.ok("You have admin access");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("error"));
        }
    }
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/super-admin")
    public ResponseEntity<?> getUserInfo(){
        try{
            return ResponseEntity.ok("You have super admin access!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseBodyMessage.error("error"));
        }
    }

}
