package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.exception.HandlerException;
import edu.alexu.cse.dripmeup.repository.UserRepository;
import edu.alexu.cse.dripmeup.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Successful() {
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail("test@example.com");
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        mockUser.setPassword(encodedPassword);

        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = userService.login("test@example.com", rawPassword);
        assertTrue(result, "Login should succeed with correct credentials");
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), "Encoded password should match the raw password");
    }

    @Test
    void testLogin_InvalidPassword() {
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail("test@example.com");
        String rawPassword = "wrongpassword";
        String encodedPassword = passwordEncoder.encode("password123");
        mockUser.setPassword(encodedPassword);

        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        boolean result = userService.login("test@example.com", rawPassword);
        assertFalse(result, "Login should fail with incorrect password");
        assertFalse(passwordEncoder.matches(rawPassword, encodedPassword), "Encoded password should not match the wrong password");
    }

    @Test
    void testLogin_UserNotFound() {
        String email = "nonexistent@example.com";
        String rawPassword = "password123";

        when(userRepository.findByEmail(email)).thenReturn(null);

        boolean result = userService.login(email, rawPassword);
        assertFalse(result, "Login should fail if user does not exist");
    }

    @Test
    void testSignup_EmailAlreadyExists() {
        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("existinguser@example.com");
        existingUser.setPassword("password123");

        when(userRepository.findByEmail("existinguser@example.com")).thenReturn(existingUser);

        UserEntity newUser = new UserEntity();
        newUser.setEmail("existinguser@example.com");
        newUser.setPassword("newpassword");

        Exception exception = assertThrows(HandlerException.class, () -> {
            userService.signup(newUser);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("existinguser@example.com");
    }

    @Test
    void testSignup_Successful() throws HandlerException {
        UserEntity newUser = new UserEntity();
        newUser.setEmail("newuser@example.com");
        String rawPassword = "newpassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        newUser.setPassword(encodedPassword);

        when(userRepository.findByEmail("newuser@example.com")).thenReturn(null);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        userService.signup(newUser);

        verify(userRepository, times(1)).save(newUser);
        assertEquals(encodedPassword, newUser.getPassword());
    }

    @Test
    void testIsEmailPresent_EmailExists() {
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        boolean result = userService.isEmailPresent("test@example.com");
        assertTrue(result, "Email should be present");
    }

    @Test
    void testIsEmailPresent_EmailNotExists() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        boolean result = userService.isEmailPresent(email);
        assertFalse(result, "Email should not be present");
    }

    @Test
    void testChangePassword_Successful() {
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail("test@example.com");
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        mockUser.setPassword(passwordEncoder.encode(oldPassword));

        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        UserEntity newPass = new UserEntity();
        newPass.setPassword(newPassword);

        boolean result = userService.changePassword("test@example.com", newPass);

        verify(userRepository, times(1)).save(mockUser);
        assertEquals(encodedNewPassword, mockUser.getPassword());
        assertTrue(result, "Password should be changed successfully");
    }
}