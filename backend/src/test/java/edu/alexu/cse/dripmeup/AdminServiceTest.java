package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.entity.AdminEntity;
import edu.alexu.cse.dripmeup.repository.AdminRepository;
import edu.alexu.cse.dripmeup.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAdminLogin_Success() {
        String userName = "admin";
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        AdminEntity admin = new AdminEntity();
        admin.setUserName(userName);
        admin.setPassword(encodedPassword);

        when(adminRepository.findByUserName(userName)).thenReturn(admin);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = adminService.adminLogin(userName, rawPassword);
        assertTrue(result, "Login should succeed with correct credentials");
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), "Encoded password should match the raw password");
    }

    @Test
    void testAdminLogin_WrongUsername() {
        String userName = "admin";
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        AdminEntity admin = new AdminEntity();
        admin.setUserName(userName);
        admin.setPassword(encodedPassword);

        when(adminRepository.findByUserName(userName)).thenReturn(admin);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = adminService.adminLogin("wrongUsername", rawPassword);
        assertFalse(result, "Login should fail with incorrect username");
    }

    @Test
    void testAdminLogin_WrongPassword() {
        String userName = "admin";
        String rawPassword = "password";
        String wrongPassword = "wrongPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        AdminEntity admin = new AdminEntity();
        admin.setUserName(userName);
        admin.setPassword(encodedPassword);

        when(adminRepository.findByUserName(userName)).thenReturn(admin);
        when(passwordEncoder.matches(wrongPassword, encodedPassword)).thenReturn(false);

        boolean result = adminService.adminLogin(userName, wrongPassword);
        assertFalse(result, "Login should fail with incorrect password");
    }

    @Test
    void testAdminLogin_UserNotFound() {
        String userName = "nonexistent";
        String rawPassword = "password";

        when(adminRepository.findByUserName(userName)).thenReturn(null);

        boolean result = adminService.adminLogin(userName, rawPassword);
        assertFalse(result, "Login should fail if user does not exist");
    }
}