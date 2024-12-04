package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import edu.alexu.cse.dripmeup.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        boolean result = userService.login("test@example.com", "password");
        assertTrue(result);
    }

    @Test
    public void testLoginFailureWrongPassword() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        boolean result = userService.login("test@example.com", "wrongpassword");
        assertFalse(result);
    }

    @Test
    public void testLoginFailureUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        boolean result = userService.login("test@example.com", "password");
        assertFalse(result);
    }

    @Test
    public void testSignupSuccess() {
        UserEntity user = new UserEntity();
        user.setEmail("newuser@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("newuser@example.com")).thenReturn(null);

        String result = userService.signup(user);
        assertEquals("User created successfully", result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testSignupFailureEmailExists() {
        UserEntity user = new UserEntity();
        user.setEmail("existinguser@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("existinguser@example.com")).thenReturn(user);

        String result = userService.signup(user);
        assertEquals("Email already exists", result);
        verify(userRepository, never()).save(user);
    }
}
