package edu.alexu.cse.dripmeup;
import edu.alexu.cse.dripmeup.entity.UserEntity;
import edu.alexu.cse.dripmeup.repository.UserRepository;
import edu.alexu.cse.dripmeup.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Successful() {
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        boolean result = userService.login("test@example.com", "password123");
        assertTrue(result, "Login should succeed with correct credentials");
    }

    @Test
    void testLogin_InvalidPassword() {
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        boolean result = userService.login("test@example.com", "wrongpassword");
        assertFalse(result, "Login should fail with incorrect password");
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        boolean result = userService.login("nonexistent@example.com", "password123");
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

        Exception exception = assertThrows(Exception.class, () -> {
            userService.signup(newUser);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("existinguser@example.com");
    }
}
