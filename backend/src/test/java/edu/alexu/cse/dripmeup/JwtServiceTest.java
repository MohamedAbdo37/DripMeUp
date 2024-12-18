package edu.alexu.cse.dripmeup;

import edu.alexu.cse.dripmeup.service.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    @Mock
    private UserDetails userDetails;

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
        jwtService.TOKEN_VALIDITY = 3600; // 1 hour
        jwtService.SIGNING_KEY = "lliAU4WFN5XY6BD5UE7MB5DG6DB8OWY2XAR2WY3NYU3VF6WM5FYU5GF5CE9AG4VOI5";
        jwtService.AUTHORITIES_KEY = "role";
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken("testUser", "ROLE_USER", 1L);
        assertNotNull(token);
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken("testUser", "ROLE_USER", 1L);
        String username = jwtService.extractUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    void testExtractId() {
        String token = jwtService.generateToken("testUser", "ROLE_USER", 1L);
        Long id = jwtService.extractId(token);
        assertEquals(1L, id);
    }

    @Test
    void testExtractRole() {
        String token = jwtService.generateToken("testUser", "ROLE_USER", 1L);
        String role = jwtService.extractRole(token);
        assertEquals("ROLE_USER", role);
    }

    @Test
    void testValidateToken() {
        String token = jwtService.generateToken("testUser", "ROLE_USER", 1L);
        when(userDetails.getUsername()).thenReturn("testUser");
        Boolean isValid = jwtService.validateToken(token, userDetails);
        assertTrue(isValid);
    }

}